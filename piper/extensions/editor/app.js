const extensionId = "eocdgalhckjoobllhobppaendhfjdjcl"


chrome.runtime.sendMessage(extensionId, { event: "remove_html_elements" })

window.onHtmlElementSelector = function() {
    $(".overlay").css({ display: "block" })
    $(".element-selector-dialog").css({ display: "block" })

    chrome.runtime.sendMessage(extensionId, { event: "html_elements" })
}

$(".element-selector-dialog button.cancel").on("click", function() {
    $(".overlay").css({ display: "none" })
    $(".element-selector-dialog").css({ display: "none" })
    Gumify.updateLastBlock(null)
    chrome.runtime.sendMessage(extensionId, { event: "remove_html_elements" })
})

window.saveWorkspace = function() {
    try {
        let workspace = Gumify.generate()
        localStorage.setItem("workspace", JSON.stringify(workspace))
        localStorage.setItem("variableStack", JSON.stringify(workspace.stack))
        localStorage.setItem("xml", workspace.xml)
    } catch (e) {
    }
}


$("#run").on("click", e => {
    try {
        let workspace = Gumify.generate()
        chrome.runtime.sendMessage(extensionId, { event: "start", code: JSON.stringify(workspace.code) })
    } catch (e) {
        console.log(e)
        alert("Extension invalidated. Refresh the page.")
    }
})
