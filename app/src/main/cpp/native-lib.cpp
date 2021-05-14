#include <jni.h>
#include <string>
#include <iostream>
#include <memory>
#include <stdint.h>

#include <mutex>

#include "third_party/v8.h"
#include "third_party/libplatform/v8-tracing.h"
#include "third_party/libplatform/libplatform.h"

//v8::Isolate *isolate_;

void happyLog(std::string tag, std::string log1, std::string log2) {
    std::cout<<tag<<" "<<log1<<" "<<log2;
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_example_happy001_MainActivity_stringFromJNI(
        JNIEnv* env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}

v8::Isolate *isolate_;
JNIEnv *env_;
jobject j_obj_;
v8::Local<v8::Context> context_;
//
extern "C" JNIEXPORT void JNICALL
Java_com_example_happy001_MainActivity_initJSFramework(
        JNIEnv* env,
        jobject j_obj) {
    env_ = env;
    j_obj_ = env->NewGlobalRef(j_obj); // **重要，局部转全局
    v8::Platform *platform = v8::platform::CreateDefaultPlatform();
    v8::V8::InitializePlatform(platform, true);
    v8::V8::Initialize();
    v8::Isolate::CreateParams create_params;
    create_params.array_buffer_allocator = v8::ArrayBuffer::Allocator::NewDefaultAllocator();
    isolate_ = v8::Isolate::New(create_params);
    v8::Isolate::Scope isolate_scope(isolate_);
    v8::HandleScope handle_scope(isolate_);
}

char* getFileSource(JNIEnv *env, jstring j_uri) {
    const char *uri = env->GetStringUTFChars(j_uri, NULL);
    std::ifstream jsfile(uri);
    int i = 0;
    std::vector<char> code_cache_data;
    if (jsfile) {
        printf("================\n");
        jsfile.seekg(0, std::ios::end);
        code_cache_data.resize(static_cast<size_t>(jsfile.tellg()) + 1);
        jsfile.seekg(0, std::ios::beg);
        jsfile.read(code_cache_data.data(), code_cache_data.size()); // 文件中内容读入缓存
        code_cache_data.back() = 0;
        jsfile.close();
    }
    return code_cache_data.data();
}

void registerGlobalInJs(v8::Local<v8::Context> context) {
    v8::HandleScope handle_scope(isolate_);
//    v8::Local<v8::Context> context = v8::Context::New(isolate_);
    v8::Context::Scope context_scope(context);
    v8::Local<v8::Object> global = context->Global();
//    v8::Local<v8::Value> gobj = v8::Object::New(isolate_);
    global->Set(context,v8::String::NewFromUtf8(isolate_, "global", v8::NewStringType::kNormal).FromMaybe(v8::Local<v8::String>()), global);
}

// native2js
v8::Local<v8::Function> getJsFun(const std::string& name, v8::Local<v8::Context> context) {
    happyLog("hippy::Debug", "GetJsFn name = %s", name.c_str());
    v8::HandleScope handle_scope(isolate_);
    v8::Context::Scope context_scope(context);
    v8::Local<v8::String> js_name = v8::String::NewFromUtf8(isolate_, name.c_str(), v8::NewStringType::kNormal).FromMaybe(v8::Local<v8::String>());
    v8::Local<v8::Function> js_fn = v8::Local<v8::Function>::Cast(context->Global()->Get(js_name));
    v8::Handle<v8::Value> handle_value =
            v8::Handle<v8::Value>::New(isolate_, js_fn);
    v8::Function *fn_res = v8::Function::Cast(*handle_value);
    intptr_t argument_count = 0;
    v8::Handle<v8::Value> args[argument_count];
    v8::Local<v8::Value> fncall_result = fn_res->Call(context, context->Global(), argument_count, args).ToLocalChecked();
    v8::String::Utf8Value utf8(isolate_, fncall_result);
//    printf("native2js call result=>>>", *utf8);
    happyLog("native2js", "call result=>>>", *utf8);
    return js_fn;
}

class CBTuple {
public:
    CBTuple(std::function<void(void *)> fn, void *data)
            : fn_(fn), data_(data) {}
    CBTuple(std::function<void(void *)> fn)
            : fn_(fn), data_(nullptr) {}
    std::function<void(void *)> fn_;
    void *data_;
};

class CBDataTuple {
public:
    CBDataTuple(const CBTuple &cb_tuple,
                const v8::FunctionCallbackInfo<v8::Value> &info)
            : cb_tuple_(cb_tuple), info_(info) {}
    const CBTuple &cb_tuple_;
    const v8::FunctionCallbackInfo<v8::Value> &info_;
};

// js2native native2java
static void callNative(void* data) {
    CBDataTuple* tuple = reinterpret_cast<CBDataTuple*>(data);
    const v8::FunctionCallbackInfo<v8::Value>& info = tuple->info_;
    v8::HandleScope handle_scope(isolate_);
    v8::Context::Scope context_scope(context_);
    jstring j_module_name = nullptr;
    if (info.Length() >= 1 && !info[0].IsEmpty()) {
        v8::String::Utf8Value module_name(isolate_, info[0]);
        char *module_name_c = *module_name;
        j_module_name = env_->NewStringUTF(module_name_c);
    }
    jstring j_module_func = nullptr;
    if (info.Length() >= 2 && !info[1].IsEmpty()) {
        v8::String::Utf8Value module_func(isolate_, info[1]);
        char *j_module_func_c = *module_func;
        j_module_func = env_->NewStringUTF(j_module_func_c);
    }
    jstring j_cb_id = nullptr;
    if (info.Length() >= 3 && !info[2].IsEmpty()) {
        v8::String::Utf8Value cb_id(isolate_, info[2]);
        j_cb_id = env_->NewStringUTF(*cb_id);
    }
    jbyteArray j_params_str = nullptr;
    v8::Handle<v8::Object> global = context_->Global();
    v8::Handle<v8::Value> JSON = global->Get(
            v8::String::NewFromUtf8(isolate_, "JSON", v8::NewStringType::kNormal)
                    .FromMaybe(v8::Local<v8::String>()));
    v8::Handle<v8::Value> fun = v8::Handle<v8::Object>::Cast(JSON)->Get(
            v8::String::NewFromUtf8(isolate_, "stringify",
                                    v8::NewStringType::kNormal)
                    .FromMaybe(v8::Local<v8::String>()));
    v8::Handle<v8::Value> argv[1] = {info[3]};
    v8::Handle<v8::Value> s =
            v8::Handle<v8::Function>::Cast(fun)->Call(JSON, 1, argv);

    v8::String::Utf8Value json(isolate_, s);
    int str_len = strlen(*json);
    j_params_str =
            env_->NewByteArray(str_len);
    env_->SetByteArrayRegion(
            j_params_str, 0, str_len,
            reinterpret_cast<const jbyte*>(*json));
//    JNIEnvironment::AttachCurrentThread()->CallVoidMethod(
//            runtime->GetBridge()->GetObj(),
//            JNIEnvironment::GetInstance()->wrapper_.call_natives_method_id,
//            j_module_name, j_module_func, j_cb_id, j_params_str);
    jclass hippy_bridge_cls =
            env_->FindClass("com/example/happy001/MainActivity");
    jmethodID call_natives_method_id = env_->GetMethodID(hippy_bridge_cls, "js2nativeFun", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;[B)V");
//    RegisterNativeBinding=>fn_template=>NativeCallbackFunc=>fn_
//    std::function<void(void*)>
    env_->CallVoidMethod(j_obj_, call_natives_method_id, j_module_name, j_module_func, j_cb_id, j_params_str);
}

void callNative2(void* data) {
    std::cout << "=============" << std::endl;
}

void NativeCallbackFunc(const v8::FunctionCallbackInfo<v8::Value>& info) {
    auto data = info.Data().As<v8::External>();
    if (data.IsEmpty()) {
        happyLog("hippy::Error", "NativeCallbackFunc data is empty", "");
        info.GetReturnValue().SetUndefined();
        return;
    }

    CBTuple* cb_tuple = reinterpret_cast<CBTuple*>(data->Value());
    CBDataTuple data_tuple(*cb_tuple, info);
    std::function<void(void *)> fn_ = nullptr;
    fn_ = cb_tuple->fn_;
    try {
        callNative(static_cast<void*>(&data_tuple));
    } catch (const std::bad_function_call& e) {
        std::cout << "=============" << e.what() << std::endl;
    }

}

// native bind
void RegisterNativeBinding(const std::string& name, std::function<void(void *)> fn, v8::Handle<v8::Context> context) {
    v8::HandleScope handle_scope(isolate_);
    v8::Context::Scope context_scope(context);
    std::unique_ptr<CBTuple> data_tuple_ = std::make_unique<CBTuple>(fn);
    v8::Local<v8::FunctionTemplate> fn_template = v8::FunctionTemplate::New(
            isolate_, NativeCallbackFunc,
            v8::External::New(isolate_, static_cast<void*>(data_tuple_.get())));
    fn_template->RemovePrototype();
    context->Global()
            ->Set(context,
                  v8::String::NewFromUtf8(isolate_, name.c_str(),
                                          v8::NewStringType::kNormal)
                          .FromMaybe(v8::Local<v8::String>()),
                  fn_template->GetFunction())
            .ToChecked();
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_example_happy001_MainActivity_runScriptFromUri(
        JNIEnv *env,
        jobject j_obj,
        jstring j_uri,
        jstring j_source) {
    printf("uri path::::", j_uri);

//    std::make_shared<JavaRef>(env, object)； // bridge

    v8::Isolate::Scope isolate_scope(isolate_);
    v8::HandleScope handle_scope(isolate_);
    v8::Local<v8::Context> context = v8::Context::New(isolate_);
    context_ = context;
    v8::Context::Scope context_scope(context);
    registerGlobalInJs(context);
    RegisterNativeBinding("js2nativeFun", callNative2, context);
    const char *sourceStr = env->GetStringUTFChars(j_source, 0);//getFileSource(env, j_uri);
    v8::Local<v8::String> source = v8::String::NewFromUtf8(isolate_, sourceStr, v8::NewStringType::kNormal).FromMaybe(v8::Local<v8::String>());
    v8::Local<v8::Script> script = v8::Script::Compile(context, source).ToLocalChecked();
    v8::Local<v8::Value> result = script->Run(context).ToLocalChecked();
    v8::String::Utf8Value utf8(isolate_, result);
    printf("js result=>>>", *utf8);

    v8::Local<v8::Function> native2jsFun = getJsFun("native2jsFun", context);

    return env->NewStringUTF(*utf8);
}

