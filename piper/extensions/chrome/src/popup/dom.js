const Stack = {
    events: []
}

class Dom {
    constructor(selector) {
        this.selector = selector
        if (selector instanceof HTMLElement || selector instanceof Document) {
            this.push(selector)
        } else if (typeof selector === 'string' && selector.startsWith('<') && selector.endsWith('>')) {
            let el = document.createElement('div')
            el.innerHTML = selector
            this.push(el.firstChild)
        } else if (typeof selector === 'string') {
            let elements = document.querySelectorAll(selector)
            for (let i = 0; i < elements.length; i++) {
                this.push(elements[i])
            }
        } else if (selector instanceof Dom) {
            this.push(...selector)
        } else if (selector instanceof Array) {
            for (let i = 0; i < selector.length; i++) {
                this.push(selector[i])
            }
        } else {
            throw new Error('Invalid selector')
        }
    }

    get(index) {
        return new Dom(this[index])
    }

    append(html) {
        let dom = new Dom(html)
        for (let el of this) {
            for (let el2 of dom) {
                el.appendChild(el2)
            }
        }
        return this
    }

    prepend(html) {
        let dom = new Dom(html)
        for (let el of this) {
            for (let el2 of dom) {
                el.insertBefore(el2, el.firstChild)
            }
        }
        return this
    }

    addClass(className) {
        for (let el of this) {
            el.classList.add(className)
        }
        return this
    }

    removeClass(className) {
        for (let el of this) {
            el.classList.remove(className)
        }
        return this
    }

    toggleClass(className) {
        for (let el of this) {
            el.classList.toggle(className)
        }
        return this
    }

    hasClass(className) {
        return this[0].classList.contains(className)
    }

    /* perform a querySelector inside the element */
    find(selector) {
        let elements = []
        for (let element of this) {
            elements = elements.concat(Array.from(element.querySelectorAll(selector)))
        }
        return new Dom(elements)
    }

    /* returns element that can be matched with given selector */
    filter(selector) {
        let elements = []
        for (let el in this) {
            let box = document.createElement("div")
            box.appendChild(this[el])
            let matches = box.querySelector(selector)
            if (matches) {
                elements.push(matches)
            }
        }
        return new Dom(elements)
    }


    on(event, callback) {
        let events = event.split(" ")
        for (let element of this) {
            for (let event of events) {
                element.addEventListener(event, callback)
                Stack.events.push({ element, event, callback })
            }
        }
        return this
    }

    off(event) {
        let events = event.split(" ")
        for (let element of this) {
            for (let event of events) {
                let e = Stack.events.find(e => e.element === element && e.event === event)
                if (e) {
                    element.removeEventListener(event, e.callback)
                    Stack.events.splice(Stack.events.indexOf(e), 1)
                }
            }
        }
        return this
    }

    clear() {
        for (let element of this) {
            let e = Stack.events.find(e => e.element === element)
            if (e) {
                element.removeEventListener(e.event, e.callback)
                Stack.events.splice(Stack.events.indexOf(e), 1)
            }
        }
    }

    children() {
        let elements = []
        let el = this[0]
        if (el) {
            elements = Array.from(el.children)
        }
        return new Dom(elements)
    }

    parent() {
        return new Dom(this[0].parentElement)
    }

    html(html) {
        if (html === undefined) {
            return this[0].innerHTML
        }
        for (let element of this) {
            element.innerHTML = html
        }
        return this
    }

    text(text) {
        if (text === undefined) {
            return this[0].innerText
        }
        for (let element of this) {
            element.innerText = text
        }
        return this
    }

    val(value) {
        if (value === undefined) {
            return this[0].value
        }
        for (let element of this) {
            element.value = value
        }
        return this
    }

    attr(name, value) {
        if (value === undefined) {
            return this[0].getAttribute(name)
        }
        for (let element of this) {
            element.setAttribute(name, value)
        }
        return this
    }

    removeAttr(name) {
        for (let element of this) {
            element.removeAttribute(name)
        }
        return this
    }

    remove() {
        for (let element of this) {
            element.remove()
        }
        return this
    }

    css(name, value) {
        if (value === undefined) {
            if (name instanceof Object) {
                for (let element of this) {
                    for (let key in name) {
                        element.style[key] = name[key]
                    }
                }
            } else {
                return getElementStyle(this[0], name)
            }
        }
        for (let element of this) {
            element.style[name] = value
        }
        return this
    }

    each(callback) {
        for (let element of this) {
            callback(element)
        }
        return this
    }

    enable() {
        for (let element of this) {
            element.disabled = false
        }
        return this
    }

    disable() {
        for (let element of this) {
            element.disabled = true
        }
        return this
    }

    extend(selector) {
        let gum = new Dom(selector)
        for (let element of gum.elements) {
            if (this.indexOf(element) === -1) {
                this.push(element)
            }
        }
    }

    first() {
        return new Dom(this[0])
    }

    last() {
        return new Dom(this[this.length - 1])
    }

    hide() {
        for (let element of this) {
            element.style.display = "none"
        }
        return this
    }

    show() {
        for (let element of this) {
            element.style.display = "block"
        }
        return this
    }

    height(value) {
        if (value === undefined) {
            return this[0].offsetHeight
        }
        for (let element of this) {
            element.style.height = value
        }
        return this
    }

    width(value) {
        if (value === undefined) {
            return this[0].offsetWidth
        }
        for (let element of this) {
            element.style.width = value
        }
        return this
    }

    innerWidth() {
        return this[0].clientWidth
    }

    innerHeight() {
        return this[0].clientHeight
    }

    disabled() {
        return this[0].disabled
    }

    offset() {
        let rect = this[0].getBoundingClientRect()
        return {
            top: rect.top + document.body.scrollTop,
            left: rect.left + document.body.scrollLeft
        }
    }

    position() {
        let rect = this[0].getBoundingClientRect()
        return {
            top: rect.top,
            left: rect.left
        }
    }

    scrollTop(value) {
        if (value === undefined) {
            return this[0].scrollTop
        }
        for (let element of this) {
            element.scrollTop = value
        }
        return this
    }

    scrollLeft(value) {
        if (value === undefined) {
            return this[0].scrollLeft
        }
        for (let element of this) {
            element.scrollLeft = value
        }
        return this
    }

    scrollWidth() {
        return this[0].scrollWidth
    }

    scrollHeight() {
        return this[0].scrollHeight
    }

    offsetParent() {
        return new Dom(this[0].offsetParent)
    }

    siblings() {
        let elements = []
        let el = this[0]
        let parent = el.parentElement
        for (let child of parent.children) {
            if (child !== el) {
                elements.push(child)
            }
        }
        return new Dom(elements)
    }
}

Dom.prototype.__proto__ = Array.prototype

function $(selector) {
    return new Dom(selector)
}

export { $ }
