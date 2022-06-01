window.attrsToString = function(el, attrs) {
    var s = "";
    for (var i = 0; i < attrs.length; i++) {
        var attr = attrs[i];
        if (attr.name === "href") {
            s += (attr.name + "=" + el.href + ",");
        } else if (attr.name === "src") {
            s += (attr.name + "=" + el.currentSrc + ",");
        } else if (attr.name === "action") {
            s += (attr.name + "=" + el.action + ",");
        } else {
            s += (attr.name + "=" + attr.value + ",");
        }
    }
    return s.slice(0, -1);
}