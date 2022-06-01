window.Gumify = {}

Blockly.HSV_SATURATION = 1.0 
// Blockly.HSV_VALUE = 0.4
Blockly.Scrollbar.scrollbarThickness = 10


try {
    window.Gumify.stack = JSON.parse(localStorage.getItem("variableStack"))
} catch(e) {
    window.Gumify.stack = {}
}
window.Gumify.lastBlockId = null


window.getTextColor = () => {
    if (localStorage.getItem("isDark") === "true") {
        return "#fff"
    }
    return "#31302e"
}

let darkTheme = Blockly.Theme.defineTheme('PiperDarkTheme', {
    base: Blockly.Themes.Classic,
    componentStyles: {
        workspaceBackgroundColour: "#31302e",
        toolboxBackgroundColour: "#3c3a38",
        toolboxForegroundColour: "#eee",
        flyoutBackgroundColour: "rgba(60, 58, 56, 0.8)",
    },
    fontStyle: {
        size: 12
    },
})

let lightTheme = Blockly.Theme.defineTheme('PiperLightTheme', {
    base: Blockly.Themes.Classic,
    componentStyles: {
        workspaceBackgroundColour: "#fff",
        toolboxBackgroundColour: "#eee",
        toolboxForegroundColour: "#333",
        flyoutBackgroundColour: "rgba(238, 238, 238, 0.8)",
    },
    fontStyle: {
        size: 12
    },
})


function getTheme() {
    if (localStorage.getItem("isDark") === "true") {
        return darkTheme
    }
    return lightTheme
}


window.workspace = Blockly.inject('blockly-div', {
    media: 'media/',
    toolbox: BLOCKLY_TOOLBOX_XML['standard'],
    zoom: {controls: true},
    move: {
        drag: true,
        wheel: false
    },
    comments: false,
    theme: getTheme()
})

window.switchTheme = isDark => {
    localStorage.setItem("xml", Blockly.Xml.domToText(Blockly.Xml.workspaceToDom(workspace)))
    localStorage.setItem("isDark", isDark)
    location.reload()
} 


Blockly.JavaScript.init(workspace)
Blockly.JavaScript.workspaceToCode = function(workspace) {
    var code = "["
    var blocks = workspace.getTopBlocks(true)
    for (var i = 0, block; block = blocks[i]; i++) {
        if (block.disabled) continue
        let statement = Blockly.JavaScript.blockToCode(block)
        if (statement instanceof Array) {
            statement = statement[0]
        }
        if (statement.endsWith(',')) {
            statement = statement.slice(0, -1)
        }
        code += statement + ','
    }
    if (code.endsWith(',')) {
        code = code.slice(0, -1)
    }
    code += "]"
    return code
}

setTimeout(() => {
    try {
        window.Gumify.stack = JSON.parse(localStorage.getItem("variableStack"))
    } catch(e) {
        window.Gumify.stack = {}
    }

    try {
        Blockly.Xml.domToWorkspace(Blockly.Xml.textToDom(localStorage.getItem("xml")), workspace)
        if (!window.isExtension) {
            localStorage.setItem("xml", null)
        }
    } catch(e) {}
}, 500)

let isToolbarToggleVisible = localStorage.getItem("isToolbarToggleVisible") === "true"
workspace.addChangeListener(function(e) {
    if (window.Gumify.stack === null) {
        window.Gumify.stack = {}
    }

    if (e.element === "dragStop" || e.element === "dragStart") {
        updateToolbarIconsColor()
        window.saveWorkspace()
    }

    if (e.element === "selected" && e.group === "" && e.oldValue !== null && e.newValue !== null) {
        try {
            let block = workspace.getBlockById(e.oldValue)
            if (block.type === "html_elements" && Gumify.stack[e.newValue] === undefined) {
                Gumify.stack[e.newValue] = Gumify.stack[e.oldValue]
            }
        } catch(e) {}
    }
    if (e.element === "dragStop" && Gumify.stack[e.blockId] === undefined) {
        try {
            let block = workspace.getBlockById(e.blockId)
            Gumify.lastBlockId = block.id
            if (block.type === "html_elements") {
                Gumify.stack[e.blockId] = ""
                if (window.Native) {
                    window.Native.openBrowser()
                } else {
                    if (window.onHtmlElementSelector) window.onHtmlElementSelector()
                }
            }
        } catch(e) {}
    }

    if (e.element === "dragStart" && isToolbarToggleVisible) {
        workspace.getToolbox().width = 0;
		workspace.deleteAreaToolbox_.right = 0;
		document.querySelector(".blocklyToolboxDiv").style.display = "none";
		$(".blocklyTreeHandle").css("left", "0px")
    }
})

let genBtn = document.querySelector("#gen")
if (genBtn !== null) {
    genBtn.addEventListener("click", function() {
        var code = Blockly.JavaScript.workspaceToCode(workspace)
        console.log(code)
        document.querySelector("#code").textContent = code
        localStorage.setItem("xml", Blockly.Xml.domToText(Blockly.Xml.workspaceToDom(workspace)))
    })
}

Gumify.generate = () => {
    let code = JSON.parse(Blockly.JavaScript.workspaceToCode(workspace))
    let xml = Blockly.Xml.workspaceToDom(workspace)
    let stack = Gumify.stack

    for (let key in stack) {
        if (workspace.getBlockById(key) === null) {
            delete stack[key]
        }
    }

    return {
        code: code,
        xml: Blockly.Xml.domToText(xml),
        stack: stack
    }
}

Gumify.compile = () => {
    let code = Blockly.JavaScript.workspaceToCode(workspace)
    return JSON.parse(code)
}

Gumify.updateLastBlock = value => {
    if (value === null || value === "null" || value === "") {
        let block = workspace.getBlockById(Gumify.lastBlockId)
        block.dispose()
        return null
    }
    let selector = atob(value)
    Gumify.stack[Gumify.lastBlockId] = selector
    return selector
}

Gumify.updateWorkspace = (xml, stack) => {
    let xmlFinal = decodeURIComponent(escape(window.atob(xml)))
    let stackFinal = decodeURIComponent(escape(window.atob(stack)))
    Gumify.stack = JSON.parse(stackFinal)
    Blockly.Xml.domToWorkspace(Blockly.Xml.textToDom(xmlFinal), workspace)
}
