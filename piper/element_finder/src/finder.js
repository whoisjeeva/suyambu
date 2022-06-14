class ElementFinder {
    constructor() {
        this.elements = []
    }

    init() {
        document.addEventListener('click', function (e) {
            e.stopPropagation();
        }, true)
        this.bindLongPressEvent()
    }

    findTree(el) {
        let currentEl = el
        let tree = []
        let nodes = Array.prototype.slice.call(currentEl.parentElement.children)
        let nth = nodes.indexOf(currentEl) + 1

        tree.push({
            tagName: currentEl.tagName.toLowerCase(),
            nth: nth,
            attrs: this._attrToArray(currentEl, currentEl.attributes)
        })

        while (true) {
            currentEl = currentEl.parentElement
            let parentTagName = currentEl.tagName.toLowerCase()
            if (parentTagName === "body") {
                break
            }

            nodes = Array.prototype.slice.call(currentEl.parentElement.children)
            nth = nodes.indexOf(currentEl) + 1
            tree.push({
                tagName: parentTagName,
                nth: nth,
                attrs: this._attrToArray(currentEl, currentEl.attributes)
            })
        }
        tree.reverse()
        return tree
    }

    toSelector(tree) {
        let selector = ""
        for (let i = 0; i < tree.length; i++) {
            let node = tree[i]
            selector += (node.tagName + ":nth-child(" + node.nth + ") ")
        }
        return selector.trim()
    }

    switchParenElement() {
        let els = []
        for (let i = 0; i < this.elements.length; i++) {
            let el = document.querySelectorAll(this.elements[i].selector)[this.elements[i].elIndex]
            if (this.elements[i].isFindSimilar) {
                let sims = this.findSimilar(el)
                for (let j = 0; j < sims.length; j++) {
                    let sim = document.querySelectorAll(sims[j].selector)[sims[j].elIndex]
                    sim.style.boxShadow = "none"
                }
            }
            let parentEl = el.parentElement
            let parentTree = this.findTree(parentEl)
            let parentSelector = this.toSelector(parentTree)
            let parentAllEls = document.querySelectorAll(parentSelector)
            let parentCurrentElIndex = 0
            for (let j = 0; j < parentAllEls.length; j++) {
                if (parentAllEls[j] === el) {
                    parentCurrentElIndex = j
                    break
                }
            }
            el.style.boxShadow = "none"
            parentEl.style.boxShadow = "0 0 0 2px yellow"
            if (this.elements[i].isFindSimilar) {
                let sims = this.findSimilar(parentEl)
                for (let j = 0; j < sims.length; j++) {
                    let sim = document.querySelectorAll(sims[j].selector)[sims[j].elIndex]
                    sim.style.boxShadow = "0 0 0 2px yellow"
                }
            }
            els.push({
                selector: parentSelector,
                elIndex: parentCurrentElIndex,
                attrs: this._attrToArray(parentEl, parentEl.attributes),
                isFindSimilar: this.elements[i].isFindSimilar,
                xpath: this.createXpath(parentEl)
            })
        }
        if (els.length > 0) {
            this.elements = els
            Native.showAlertWithElements(JSON.stringify(this.elements))
            return true
        }
        return false
    }

    getElementByXpath(path) {
        return document.evaluate(path, document, null, XPathResult.FIRST_ORDERED_NODE_TYPE, null).singleNodeValue
    }

    createSingleXpath(el) {
        var ix = 0
        var siblings = el.parentNode.childNodes
        for (var i = 0; i < siblings.length; i++) {
            var sibling = siblings[i]
            if (sibling === el) {
                return el.tagName + '[' + (ix + 1) + ']'
            }
            if (sibling.nodeType === 1 && sibling.tagName === el.tagName) {
                ix++
            }
        }
    }

    createXpath(el) {
        let xpath = this.createSingleXpath(el);
        let parent = el.parentNode;
        while (parent.tagName !== 'BODY') {
            xpath = this.createSingleXpath(parent) + '/' + xpath;
            parent = parent.parentNode;
        }
        return "/html/body/" + xpath.toLowerCase();
    }

    findSimilar(currentEl) {
        let rect = currentEl.getBoundingClientRect()
        let width = rect.width
        let height = rect.height
        let currentTree = this.findTree(currentEl)
        let currentTagString = this._concatTagNames(currentTree)
        let currentChildren = currentEl.querySelectorAll("*")

        let allEls = document.querySelectorAll("body *")
        let similarEls = []
    
        for (let i = 0; i < allEls.length; i++) {
            let simEl = allEls[i]
            let simRect = simEl.getBoundingClientRect()
            let simWidth = simRect.width
            let simHeight = simRect.height
            let simTree = this.findTree(simEl)
            let simTagString = this._concatTagNames(simTree)
            let simChildren = simEl.querySelectorAll("*")
    
            if (
                (width === simWidth && height === simHeight && simEl.tagName === currentEl.tagName && simTagString === currentTagString) ||
                (simTagString === currentTagString && simEl.tagName === currentEl.tagName && simChildren.length === currentChildren.length)
            ) {
                let simTree = this.findTree(simEl)
                let obj = {selector: "body " + this.toSelector(simTree), elIndex: 0, attrs: this._attrToArray(simEl, simEl.attributes)}
                let els = document.querySelectorAll(obj.selector)
                for (let j = 0; j < els.length; j++) {
                    if (els[j] === simEl && simEl !== currentEl) {
                        obj.elIndex = j
                        similarEls.push(obj)
                        simEl.style.boxShadow = "0 0 0 2px yellow"
                        break
                    }
                }
            }
        }
    
        return similarEls
    }

    bindLongPressEvent() {
        (new Hammer(document)).on("press", e => {
            if (!this._isHidden(e.target)) {
                let sameElement = null
                if(this.elements.length != 0) {
                    for (let i = 0; i < this.elements.length; i++) {
                        let el = document.querySelectorAll(this.elements[i].selector)[this.elements[i].elIndex]
                        if (el === e.target) {
                            sameElement = this.elements[i]
                            e.target.style.boxShadow = "none"
                            break
                        }
                    }
                }
                
                if (sameElement !== null) {
                    this.elements.splice(this.elements.indexOf(sameElement), 1)
                } else {
                    let tree = this.findTree(e.target)
                    let selector = this.toSelector(tree)
                    let allEls = document.querySelectorAll(selector)
                    let currentElIndex = 0
                    for (let i = 0; i < allEls.length; i++) {
                        if (allEls[i] === e.target) {
                            currentElIndex = i
                            break
                        }
                    }

                    this.elements.push({ selector: "body " + selector, elIndex: currentElIndex, attrs: this._attrToArray(e.target, e.target.attributes), isFindSimilar: false, xpath: this.createXpath(e.target) })
                    e.target.style.boxShadow = "0 0 0 2px yellow"
                }

                Native.showAlertWithElements(JSON.stringify(this.elements))
            }
        }, false)
    }

    addSimilarElements() {
        let totalEls = this.elements.length
        let isSimilarFound = false

        for (let i = 0; i < totalEls; i++) {
            let originalEl = document.querySelectorAll(this.elements[i].selector)[this.elements[i].elIndex]
            let similar = this.findSimilar(originalEl)
            if (similar.length > 0) {
                isSimilarFound = true
            }
            this.elements[i].isFindSimilar = true
            for (let j = 0; j < similar.length; j++) {
                let el = document.querySelectorAll(similar[j].selector)[similar[j].elIndex]
                el.style.boxShadow = "0 0 0 2px yellow"
            }
        }

        return isSimilarFound
    }

    looksGood() {
        return "Looks good! total" + this.elements.length + " element(s) found."
    }

    getSelectedElements() {
        if (this.elements.length === 0) {
            return null
        }
        return JSON.stringify(this.elements)
    }

    findNearestElement(tagName) {
        if (this.elements.length === 0) {
            Native.showToast("please select an element first")
            return
        }
        let currentEl = document.querySelectorAll(this.elements[0].selector)[this.elements[0].elIndex]
        let currentElHolder = currentEl
        let foundEl = null
        let tagNameToFind = tagName.toLowerCase()

        while (true) {
            let isFound = false
            let children = currentElHolder.querySelectorAll("*")
            for (let i = 0; i < children.length; i++) {
                if (children[i].tagName.toLowerCase() === tagNameToFind && children[i] !== currentEl && !this._isHidden(children[i])) {
                    isFound = true
                    foundEl = children[i]
                    break
                }
            }

            if (isFound || currentElHolder.tagName.toLowerCase === "body") {
                break
            }
            currentElHolder = currentElHolder.parentElement
        }

        if (foundEl !== null) {
            let tree = this.findTree(foundEl)
            let selector = this.toSelector(tree)
            let allEls = document.querySelectorAll(selector)
            let currentElIndex = 0
            for (let i = 0; i < allEls.length; i++) {
                if (allEls[i] === foundEl) {
                    currentElIndex = i
                    break
                }
            }
            for (let i = 0; i < this.elements.length; i++) {
                let el = document.querySelectorAll(this.elements[i].selector)[this.elements[i].elIndex]
                el.style.boxShadow = "none"
            }
            this.elements = []
            this.elements.push({selector: "body " + selector, elIndex: currentElIndex, attrs: this._attrToArray(foundEl, foundEl.attributes), isFindSimilar: false, xpath: this.createXpath(foundEl)})
            Native.showAlertWithElements(JSON.stringify(this.elements))
            currentEl.style.boxShadow = "none"
            foundEl.style.boxShadow = "0 0 0 2px yellow"
        } else {
            Native.showToast("No element exist with '" + tagNameToFind + "' tag name.")
            Native.showAlertWithElements(JSON.stringify(this.elements))
            return "No element exist with '" + tagNameToFind + "' tag name."
        }
    }

    confirmElements() {
        if (this.elements.length > 0) {
            Native.confirmElements(JSON.stringify(this.elements))
        }
    }

    _isHidden(el) {
        return el.offsetParent === null
    }

    _concatTagNames(tree) {
        let s = ""
        for (let i = 0; i < tree.length; i++) {
            let node = tree[i]
            s += (node.tagName + " ")
        }
        return s.trim()
    }

    _concatAttrClassNames(attrs) {
        let s = ""
        for (let i = 0; i < attrs.length; i++) {
            let attr = attrs[i]
            if (attr.name === "class") {
                s += (attr.value + " ")
            }
        }
        return s.trim()
    }

    _concatTagNamesWithNth(tree) {
        let s = ""
        for (let i = 0; i < tree.length; i++) {
            let node = tree[i]
            s += (node.tagName + " " + node.nth + " ")
        }
        return s.trim()
    }

    _attrToArray(el, attrs) {
        let attrArray = []
        for (let i = 0; i < attrs.length; i++) {
            let attr = attrs[i]
            if (attr.name === "href") {
                attrArray.push({
                    name: attr.name,
                    value: el.href
                })
            } else if (attr.name === "src") {
                attrArray.push({
                    name: attr.name,
                    value: el.currentSrc
                })
            } else if (attr.name === "action") {
                attrArray.push({
                    name: attr.name,
                    value: el.action
                })
            } else {
                attrArray.push({
                    name: attr.name,
                    value: attr.value
                })
            }
        }
        return attrArray
    }
}


window.elementFinder = new ElementFinder()
