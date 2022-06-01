import { stringify } from "../util"


export default async function(statement, onError) {
    try {
        let pointer = statement.pointer
        let list = await this.executeStatement(stringify(pointer.list))
        let index = await this.executeStatement(stringify(pointer.index))
        index = parseInt(index)
        let value = await this.executeStatement(stringify(pointer.value))
        let mode = stringify(pointer.mode)
        let where = stringify(pointer.where)

        if (where === "FROM_START") {
            if (mode === "SET") list[index - 1] = value
            else if (mode === "INSERT") list.splice(index - 1, 0, value)
        } else if (where === "FROM_END") {
            if (mode === "SET") list[list.length - index] = value
            else if (mode === "INSERT") list.splice(list.length - index, 0, value)
        } else if (where === "FIRST") {
            if (mode === "SET") list[0] = value
            else if (mode === "INSERT") list.splice(0, 0, value)
        } else if (where === "LAST") {
            if (mode === "SET") list[list.length - 1] = value
            else if (mode === "INSERT") list.splice(list.length - 1, 0, value)
        } else if (where === "RANDOM") {
            let rand = Math.floor(Math.random() * list.length)
            if (mode === "SET") list[rand] = value
            else if (mode === "INSERT") list.splice(rand, 0, value)
        }
    } catch (e) {
        onError(e)
    }
}
