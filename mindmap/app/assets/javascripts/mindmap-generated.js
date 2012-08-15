;window.MindMap = (function (m) {
m['showMap'] = function(x103) {
var x104 = document.createElementNS('http://www.w3.org/1999/xhtml', 'defs');
var x105 = x103.vertices;
var x49 = (0).toString();
var x129=x105.map(function(x106){
var x113 = x106.width;
var x115 = (x113).toString();
var x114 = x106.height;
var x116 = (x114).toString();
var x117 = document.createElementNS('http://www.w3.org/2000/svg', 'rect');
x117.setAttribute('width', x115);
x117.setAttribute('height', x116);
var x120 = x106.content;
var x121 = document.createTextNode(x120);
var x118 = "height: "+x114;
var x119 = x118+"px;";
var x123 = document.createElementNS('http://www.w3.org/1999/xhtml', 'p');
x123.setAttribute('style', x119);
x123.appendChild(x121);
var x125 = document.createElementNS('http://www.w3.org/2000/svg', 'foreignObject');
x125.setAttribute('x', x49);
x125.setAttribute('y', x49);
x125.setAttribute('width', x115);
x125.setAttribute('height', x116);
x125.appendChild(x123);
var x107 = x106.posx;
var x108 = "translate("+x107;
var x109 = x108+",";
var x110 = x106.posy;
var x111 = x109+x110;
var x112 = x111+")";
var x127 = document.createElementNS('http://www.w3.org/2000/svg', 'g');
x127.setAttribute('class', "vertex");
x127.setAttribute('transform', x112);
x127.appendChild(x117);
x127.appendChild(x125);
return x127
});
var x130 = x103.edges;
var x158=x130.map(function(x131){
var x132 = x131.orig;
var x133 = x132.posx;
var x134 = x132.width;
var x135 = x134 / 2;
var x136 = x133 + x135;
var x150 = (x136).toString();
var x137 = x132.posy;
var x138 = x132.height;
var x139 = x138 / 2;
var x140 = x137 + x139;
var x151 = (x140).toString();
var x141 = x131.end;
var x142 = x141.posx;
var x143 = x141.width;
var x144 = x143 / 2;
var x145 = x142 + x144;
var x152 = (x145).toString();
var x146 = x141.posy;
var x147 = x141.height;
var x148 = x147 / 2;
var x149 = x146 + x148;
var x153 = (x149).toString();
var x154 = document.createElementNS('http://www.w3.org/2000/svg', 'line');
x154.setAttribute('x1', x150);
x154.setAttribute('y1', x151);
x154.setAttribute('x2', x152);
x154.setAttribute('y2', x153);
var x156 = document.createElementNS('http://www.w3.org/2000/svg', 'g');
x156.setAttribute('class', "edge");
x156.appendChild(x154);
return x156
});
var x159 = x158.concat(x129);
return x159
}
;
m['buildMap'] = function(x0) {
var x2 = x0.edges;
var x1 = x0.vertices;
var x9=x2.map(function(x3){
var x4 = x3.origIdx;
var x5 = x1[x4];
var x6 = x3.endIdx;
var x7 = x1[x6];
var x8 = {'orig' : x5,'end' : x7};
return x8
});
var x10 = x0.name;
var x11 = x1.splice(0);
var x12 = {'name' : x10,'vertices' : x11,'edges' : x9};
return x12
}
;
return m
})(window.MindMap || {});
