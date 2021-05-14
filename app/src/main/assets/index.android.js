var test = "hello, world";
console.error("global==>", global);
global.native2jsFun = function() {
    var name = "native2jsFun";
    return name;
}
global.js2nativeFun("js2native", "test", "test", [{pid: 0, id: 1}, {pid: 1, id: 2}]);
global.js2nativeFun;