function sleep(ms) {
    return new Promise(resolve => setTimeout(resolve, ms))
}

function stringify(obj) {
    if (obj === undefined) {
        return "undefined"
    } else if (obj === null) {
        return "null"
    } else if (typeof obj === "string") {
        return obj
    }
    let str = obj.toString()
    if (str === "[object Object]") {
        return JSON.stringify(obj)
    }
    return str
}


export { sleep, stringify }
