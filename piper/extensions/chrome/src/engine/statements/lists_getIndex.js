import { stringify } from "../util"


export default async function(statement, onError) {
    try {
        let pointer = statement.pointer
        let list = await this.executeStatement(stringify(pointer.list))
        let index = await this.executeStatement(stringify(pointer.index))
        index = parseInt(index)
        let mode = stringify(pointer.mode)
        let where = stringify(pointer.where)

        if (where === "FROM_START") {
            if (mode === "GET") return list[index - 1]
            else if (mode === "GET_REMOVE") return list.splice(index - 1, 1)[0]
            else if (mode === "REMOVE") list.splice(index - 1, 1)
        } else if (where === "FROM_END") {
            if (mode === "GET") return list[list.length - index]
            else if (mode === "GET_REMOVE") return list.splice(list.length - index, 1)[0]
            else if (mode === "REMOVE") list.splice(list.length - index, 1)
        } else if (where === "FIRST") {
            if (mode === "GET") return list[0]
            else if (mode === "GET_REMOVE") return list.splice(0, 1)[0]
            else if (mode === "REMOVE") list.splice(0, 1)
        } else if (where === "LAST") {
            if (mode === "GET") return list[list.length - 1]
            else if (mode === "GET_REMOVE") return list.splice(list.length - 1, 1)[0]
            else if (mode === "REMOVE") list.splice(list.length - 1, 1)
        } else if (where === "RANDOM") {
            let rand = Math.floor(Math.random() * list.length)
            if (mode === "GET") return list[rand]
            else if (mode === "GET_REMOVE") return list.splice(rand, 1)[0]
            else if (mode === "REMOVE") list.splice(rand, 1)
        }
    } catch (e) {
        onError(e)
    }

    return null
}
