;window.MindMap = (function (m) {
m['showMap'] = function(x106) {
var x107 = document.createElementNS('http://www.w3.org/1999/xhtml', 'defs');
var x108 = x106.vertices;
var x52 = (0).toString();
var x132=x108.map(function(x109){
var x116 = x109.width;
var x118 = (x116).toString();
var x117 = x109.height;
var x119 = (x117).toString();
var x120 = document.createElementNS('http://www.w3.org/2000/svg', 'rect');
x120.setAttribute('width', x118);
x120.setAttribute('height', x119);
var x123 = x109.content;
var x124 = document.createTextNode(x123);
var x121 = "height: "+x117;
var x122 = x121+"px;";
var x126 = document.createElementNS('http://www.w3.org/1999/xhtml', 'p');
x126.setAttribute('style', x122);
x126.appendChild(x124);
var x128 = document.createElementNS('http://www.w3.org/2000/svg', 'foreignObject');
x128.setAttribute('x', x52);
x128.setAttribute('y', x52);
x128.setAttribute('width', x118);
x128.setAttribute('height', x119);
x128.appendChild(x126);
var x110 = x109.posx;
var x111 = "translate("+x110;
var x112 = x111+",";
var x113 = x109.posy;
var x114 = x112+x113;
var x115 = x114+")";
var x130 = document.createElementNS('http://www.w3.org/2000/svg', 'g');
x130.setAttribute('class', "vertex");
x130.setAttribute('transform', x115);
x130.appendChild(x120);
x130.appendChild(x128);
return x130
});
var x133 = x106.edges;
var x161=x133.map(function(x134){
var x135 = x134.orig;
var x136 = x135.posx;
var x137 = x135.width;
var x138 = x137 / 2;
var x139 = x136 + x138;
var x153 = (x139).toString();
var x140 = x135.posy;
var x141 = x135.height;
var x142 = x141 / 2;
var x143 = x140 + x142;
var x154 = (x143).toString();
var x144 = x134.end;
var x145 = x144.posx;
var x146 = x144.width;
var x147 = x146 / 2;
var x148 = x145 + x147;
var x155 = (x148).toString();
var x149 = x144.posy;
var x150 = x144.height;
var x151 = x150 / 2;
var x152 = x149 + x151;
var x156 = (x152).toString();
var x157 = document.createElementNS('http://www.w3.org/2000/svg', 'line');
x157.setAttribute('x1', x153);
x157.setAttribute('y1', x154);
x157.setAttribute('x2', x155);
x157.setAttribute('y2', x156);
var x159 = document.createElementNS('http://www.w3.org/2000/svg', 'g');
x159.setAttribute('class', "edge");
x159.appendChild(x157);
return x159
});
var x162 = x161.concat(x132);
return x162
}
;
m['listMaps'] = function(x164, x165) {
var x167 = "/"+x165;
var x168 = x167+"/";
var x180 = [];
for(var x194 = 0 ; x194 < x164.length ; x194++){
x180.splice.apply(x180, [x180.length, 0].concat((function(x166){
var x171 = x166.content;
var x172 = x171.name;
var x173 = document.createTextNode(x172);
var x169 = x166.id;
var x170 = x168+x169;
var x175 = document.createElementNS('http://www.w3.org/1999/xhtml', 'a');
x175.setAttribute('href', x170);
x175.appendChild(x173);
var x177 = document.createElementNS('http://www.w3.org/1999/xhtml', 'li');
x177.appendChild(x175);
var x178 = [x177];
return x178
})(x164[x194])));
}
var x17 = [];
var x181 = x180.concat(x17);
var x182 = document.createElementNS('http://www.w3.org/1999/xhtml', 'ul');
for (var x195 = 0 ; x195 < x181.length ; x195++) {
x182.appendChild(x181[x195]);
}
var x183 = document.createTextNode("Create a ");
var x184 = document.createTextNode("new mind map");
var x186 = document.createElementNS('http://www.w3.org/1999/xhtml', 'strong');
x186.appendChild(x184);
var x187 = document.createTextNode(": ");
var x188 = document.createElementNS('http://www.w3.org/1999/xhtml', 'input');
x188.setAttribute('type', "text");
x188.setAttribute('placeholder', "Name");
var x190 = document.createElementNS('http://www.w3.org/1999/xhtml', 'p');
x190.appendChild(x183);
x190.appendChild(x186);
x190.appendChild(x187);
x190.appendChild(x188);
var x192 = document.createElementNS('http://www.w3.org/1999/xhtml', 'div');
x192.appendChild(x182);
x192.appendChild(x190);
return x192
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
