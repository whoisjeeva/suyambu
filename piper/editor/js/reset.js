function updateToolbarIconsColor() {
	let isVisible = false
	$(".blocklyFlyout").each(el => {
		if (!isVisible && window.getComputedStyle(el).display === "block") {
			isVisible = true
		}
	})
	if (!isVisible) {
		$(".blocklyTreeRow .material-icons").css("color", getTextColor())
	}
}


$(".blocklyTreeSeparator").css({ 
    borderBottom: "1px solid rgba(255, 255, 255, 0.1)",
    borderLeft: "none"
})

document.addEventListener("click", e => {
	updateToolbarIconsColor()
}, false)

$(".blocklyTreeRow .blocklyTreeIcon").each(el => {
	let parent = $(el).parent()
	let nodes = parent.css("border-left").split("rgb")
	// let color = "rgb" + nodes[nodes.length - 1]
	let label = parent.find(".blocklyTreeLabel").css("font-size", "0.8rem").text().trim()

	parent.css({ display: "flex",
			alignItems: "center",
			flexDirection: "column",
			justifyContent: "center",
			height: "auto",
			padding: "10px 0px",
			borderLeft: "none",
			transition: "all 0.2s ease-in-out" })
		.find(".blocklyTreeIcon")
		.css({ height: "auto", width: "auto" })

		
	let icon = document.createElement("i")
	$(icon).addClass("material-icons")

	parent.on("click", function() {
		setTimeout(function() {
			$(".blocklyTreeRow .material-icons").css("color", getTextColor())
            let width = $(".blocklyToolboxDiv").innerWidth()
			icon.style.color = "white"
            $(".blocklyFlyout").css("transform", "translate(" + width + "px, 0px)")
			updateToolbarIconsColor()
		}, 1)
	})

	
	if (label === "Browser") {
		icon.textContent = "public"
	} else if (label === "HTML") {
		icon.textContent = "code"
	} else if (label === "Events") {
		icon.textContent = "event"
	} else if (label === "File I/O") {
		icon.textContent = "folder"
	} else if (label === "Logic") {
		icon.textContent = "account_tree"
	} else if (label === "Loops") {
		icon.textContent = "repeat"
	} else if (label === "Math") {
		icon.textContent = "calculate"
	} else if (label === "Text") {
		icon.textContent = "text_fields"
	} else if (label === "Lists") {
		icon.textContent = "data_array"
	} else if (label === "Dict") {
		icon.textContent = "data_object"
	} else if (label === "Variables") {
		icon.textContent = "inventory"
	} else if (label === "Functions") {
		icon.textContent = "functions"
	} else if (label === "Utils") {
		icon.textContent = "settings"
	}


	parent.prepend(icon)
	parent.find(".blocklyTreeIcon").remove()
})




function handleColor() {
	if (localStorage.getItem("isDark") === "true") {
		return "#3c3a38"
	}
	return "#eee"
}


if (localStorage.getItem("isToolbarToggleVisible") === "true") {
	setTimeout(() => {

		let handle = $(document.createElement("div"))
		handle.addClass("blocklyTreeHandle")
		let bar1 = $(document.createElement("span"))
		bar1.addClass("blocklyTreeBar1").css("background-color", getTextColor())
		let bar2 = $(document.createElement("span"))
		bar2.addClass("blocklyTreeBar2").css("background-color", getTextColor())
		let bar3 = $(document.createElement("span"))
		bar3.addClass("blocklyTreeBar3").css("background-color", getTextColor())
		handle.append(bar1)
		handle.append(bar2)
		handle.append(bar3)
		$("#blockly-div").append(handle)

		let toolboxWidth = workspace.getToolbox().width

		workspace.getToolbox().width = 0;
		workspace.deleteAreaToolbox_.right = 0;
		document.querySelector(".blocklyToolboxDiv").style.display = "none";
		handle.css("left", "0px")

		handle.css({
			borderRadius: "0px 10px 10px 0px",
			backgroundColor: handleColor(),
			left: document.querySelector(".blocklyToolboxDiv").offsetWidth + "px"
		}).on("click", e => {
			// workspace.getToolbox().width === 0
			if ($(".blocklyToolboxDiv").css("display") === "none") {
                // workspace.getToolbox().width = toolboxWidth;
                document.querySelector(".blocklyToolboxDiv").style.display = "block";
				handle.css("left", document.querySelector(".blocklyToolboxDiv").offsetWidth + "px")
            } else {
				workspace.getToolbox().width = 0;
				workspace.deleteAreaToolbox_.right = 0;
                document.querySelector(".blocklyToolboxDiv").style.display = "none";
				handle.css("left", "0px")
            }
		})

	}, 300)
}


// $(".blocklyTrash").css({
// 	transform: "translate(" + (window.innerWidth - 80) + "px, 80px)",
// })