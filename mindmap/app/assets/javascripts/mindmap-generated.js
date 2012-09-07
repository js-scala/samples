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
var x135 = Math.floor(x134 / 2);
var x136 = x133 + x135;
var x150 = (x136).toString();
var x137 = x132.posy;
var x138 = x132.height;
var x139 = Math.floor(x138 / 2);
var x140 = x137 + x139;
var x151 = (x140).toString();
var x141 = x131.end;
var x142 = x141.posx;
var x143 = x141.width;
var x144 = Math.floor(x143 / 2);
var x145 = x142 + x144;
var x152 = (x145).toString();
var x146 = x141.posy;
var x147 = x141.height;
var x148 = Math.floor(x147 / 2);
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
m['listMaps'] = function(x161) {
var x174 = [];
for(var x188 = 0 ; x188 < x161.length ; x188++){
x174.splice.apply(x174, [x174.length, 0].concat((function(x162){
var x165 = x162.content;
var x166 = x165.name;
var x167 = document.createTextNode(x166);
var x163 = x162.id;
var x164 = "/"+x163;
var x169 = document.createElementNS('http://www.w3.org/1999/xhtml', 'a');
x169.setAttribute('href', x164);
x169.appendChild(x167);
var x171 = document.createElementNS('http://www.w3.org/1999/xhtml', 'li');
x171.appendChild(x169);
var x172 = [x171];
return x172
})(x161[x188])));
}
var x14 = [];
var x175 = x174.concat(x14);
var x176 = document.createElementNS('http://www.w3.org/1999/xhtml', 'ul');
for (var x189 = 0 ; x189 < x175.length ; x189++) {
x176.appendChild(x175[x189]);
}
var x177 = document.createTextNode("Create a ");
var x178 = document.createTextNode("new mind map");
var x180 = document.createElementNS('http://www.w3.org/1999/xhtml', 'strong');
x180.appendChild(x178);
var x181 = document.createTextNode(": ");
var x182 = document.createElementNS('http://www.w3.org/1999/xhtml', 'input');
x182.setAttribute('type', "text");
x182.setAttribute('placeholder', "Name");
var x184 = document.createElementNS('http://www.w3.org/1999/xhtml', 'p');
x184.appendChild(x177);
x184.appendChild(x180);
x184.appendChild(x181);
x184.appendChild(x182);
var x186 = document.createElementNS('http://www.w3.org/1999/xhtml', 'div');
x186.setAttribute('class', "list-maps");
x186.appendChild(x176);
x186.appendChild(x184);
return x186
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
m['setupExploration'] = function(x0, x1, x2, x3) {
var x4 = document.querySelector(".workspace");
var x6 = document.querySelector("svg");
if (x4 !== null) {
var x5 = x4;
var x11 = function(x12,x13,x14) {
var x17 = x14;
var x16 = x13;
var x15 = x12;
var x18 = Math.floor(x15 / 100);
var x19 = "scale("+x18;
var x20 = x19+") translate(";
var x21 = x20+x16;
var x22 = x21+" ";
var x23 = x22+x17;
var x24 = x23+")";
var x25 = x5.setAttribute("transform", x24);
var x28 = x3(x15,x16,x17);
var x26 = {'s' : x15,'tx' : x16,'ty' : x17};
var x29 = history.replaceState(x26, "", x28);
return x29
}
if (x6 !== null) {
var x7 = x6;
var x8 = x0;
var x9 = x1;
var x10 = x2;
var x48 = window.addEventListener('popstate', function (x31) {
var x32 = x31.state;
if (x32 !== null) {
var x33 = x32;
var x34 = x33.s;
x8 = x34
var x36 = x33.tx;
x9 = x36
var x38 = x33.ty;
x10 = x38
var x40 = x8;
var x41 = x9;
var x42 = x10;
var x44 = x11(x40,x41,x42);
}
var x46 = undefined;
}, false);
var x80 = x7.addEventListener('mousewheel', function (x49) {
var x50 = x8;
var x51 = x49.wheelDeltaY;
var x52 = Math.floor(x51 / 16);
var x53 = x50 + x52;
var x54 = x53 > 0;
var x78
if (x54) {
var x55 = x9;
var x56 = x49.offsetX;
var x57 = x56 * 100;
var x58 = Math.floor(x57 / x50);
var x59 = x55 - x58;
var x60 = Math.floor(x57 / x53);
var x61 = x59 + x60;
x9 = x61
var x63 = x10;
var x64 = x49.offsetY;
var x65 = x64 * 100;
var x66 = Math.floor(x65 / x50);
var x67 = x63 - x66;
var x68 = Math.floor(x65 / x53);
var x69 = x67 + x68;
x10 = x69
x8 = x53
var x72 = x8;
var x73 = x9;
var x74 = x10;
var x76 = x11(x72,x73,x74);
x78=x76
} else {
x78=undefined
}
}, false);
var x81 = false;
var x82 = 0;
var x83 = 0;
var x97 = window.addEventListener('mousedown', function (x84) {
var x85 = x84.target;
var x86 = x85;
var x87 = x86.tagName;
var x88 = x87=="svg";
var x95
if (x88) {
x81 = true
var x90 = x84.offsetX;
x82 = x90
var x92 = x84.offsetY;
x83 = x92
x95=undefined
} else {
x95=undefined
}
}, false);
var x126 = window.addEventListener('mousemove', function (x98) {
var x99 = x81;
var x124
if (x99) {
var x100 = x9;
var x102 = x82;
var x105 = x8;
var x101 = x98.offsetX;
var x103 = x101 - x102;
var x104 = x103 * 100;
var x106 = Math.floor(x104 / x105);
var x107 = x100 + x106;
x9 = x107
var x109 = x10;
var x111 = x83;
var x110 = x98.offsetY;
var x112 = x110 - x111;
var x113 = x112 * 100;
var x114 = Math.floor(x113 / x105);
var x115 = x109 + x114;
x10 = x115
x82 = x101
x83 = x110
var x119 = x9;
var x120 = x10;
var x122 = x11(x105,x119,x120);
x124=x122
} else {
x124=undefined
}
}, false);
var x130 = window.addEventListener('mouseup', function (x127) {
x81 = false
}, false);
}
var x132 = undefined;
}
var x134 = undefined;
return x134
}
;
return m
})(window.MindMap || {});
