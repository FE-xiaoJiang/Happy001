global.native2jsFun = function() {
    var name = "native2jsFun";
    return name;
}
global.js2nativeFun("UIManagerModule", "createNode", "test", [{pid: 0, id: 1, props: {top: 100, text: "hello, bro, im in sub"}}, {pid: 1, id: 2, props: {top: 200, text: "你好，老妹，我也是"}}]);