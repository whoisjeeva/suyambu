import { stringify } from "../util"



function subsequenceFromStartFromEnd(sequence, at1, at2) {
    let start = at1
    let end = sequence.length - 1 - at2 + 1
    return sequence.slice(start, end)
}

function subsequenceFromStartLast(sequence, at1) {
    let start = at1
    let end = sequence.length - 1 + 1
    return sequence.slice(start, end)
}

function subsequenceFromEndFromStart(sequence, at1, at2) {
    let start = sequence.length - 1 - at1
    let end = at2 + 1
    return sequence.slice(start, end)
}

function subsequenceFromEndFromEnd(sequence, at1, at2) {
    let start = sequence.length - 1 - at1
    let end = sequence.length - 1 - at2 + 1
    return sequence.slice(start, end)
}

function subsequenceFromEndLast(sequence, at1) {
    let start = sequence.length - 1 - at1
    let end = sequence.length - 1 + 1
    return sequence.slice(start, end)
}

function subsequenceFirstFromEnd(sequence, at2) {
    let start = 0
    let end = sequence.length - 1 - at2 + 1
    return sequence.slice(start, end)
}


export default async function(statement, onError) {
    try {
        let pointer = statement.pointer
        let value = await this.executeStatement(stringify(pointer.value))
        value = stringify(value)
        let at1 = await this.executeStatement(stringify(pointer.at1))
        at1 = parseInt(stringify(at1))
        let at2 = await this.executeStatement(stringify(pointer.at2))
        at2 = parseInt(stringify(at2))

        let action1 = stringify(pointer.action1)
        let action2 = stringify(pointer.action2)

        if (action1 === "FROM_START" && action2 === "FROM_START") {
            return value.slice(at1-1, at2)
        } else if (action1 === "FROM_START" && action2 === "FROM_END") {
            return subsequenceFromStartFromEnd(value, at1-1, at2-1)
        } else if (action1 === "FROM_START" && action2 === "LAST") {
            return subsequenceFromStartLast(value, at1-1)
        } else if (action1 === "FROM_END" && action2 === "FROM_START") {
            return subsequenceFromEndFromStart(value, at1-1, at2-1)
        } else if (action1 === "FROM_END" && action2 === "FROM_END") {
            return subsequenceFromEndFromEnd(value, at1-1, at2-1)
        } else if (action1 === "FROM_END" && action2 === "LAST") {
            return subsequenceFromEndLast(value, at1-1)
        } else if (action1 === "FIRST" && action2 === "FROM_START") {
            return value.slice(0, at2)
        } else if (action1 === "FIRST" && action2 === "FROM_END") {
            return subsequenceFirstFromEnd(value, at2-1)
        } else {
            return value
        }
    } catch (e) {
        onError(e)
    }

    return null
}
