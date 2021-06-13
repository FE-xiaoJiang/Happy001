var test = "hello, world";
console.error("global==>", global);
global.native2jsFun = function() {
    var name = "native2jsFun";
    return name;
}
global.js2nativeFun("UIManagerModule", "createNode", "test", [{pid: 0, id: 1, props: {width: 200, height: 60, top: 100, text: "hello, bro", backgroundColor: "#4ea1db"}}, {pid: 1, id: 2, props: {width: 200, height: 60, top: 200, text: "你好，老妹", backgroundColor: "#4ea1db"}}]);
//setTimeout(function() {
//    global.js2nativeFun("UIManagerModule", "updateNode", "test", [{pid: 0, id: 1, top: 0, props: {top: 100, text: "hello, bro, a u there"}}, {pid: 1, id: 2, props: {top: 200, text: "sorry, im gone"}}]);
//    console.log("...");
//}, 3000);

global.js2nativeFun;

//{type: "div", props: {top: 100, text: "hello, bro"}, child: {}}