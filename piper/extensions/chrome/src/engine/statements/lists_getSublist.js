import { stringify } from "../util"


export default async function(statement, onError) {
    try {
        let pointer = statement.pointer
        let list = await this.executeStatement(stringify(pointer.list))
        let where1 = stringify(pointer.where1)
        let where2 = stringify(pointer.where2)
        let at1 = await this.executeStatement(stringify(pointer.at1))
        at1 = parseInt(at1)
        let at2 = await this.executeStatement(stringify(pointer.at2))
        at2 = parseInt(at2)

        if (where1 === "FROM_START") {
            if (where2 === "FROM_START") return list.slice(at1 - 1, at2)
            else if (where2 === "FROM_END") return list.slice(at1 - 1, list.length - (at2 - 1))
            else if (where2 === "LAST") return list.slice(at1 - 1, list.length)
        } else if (where1 === "FROM_END") {
            if (where2 === "FROM_START") return list.slice(list.length - at1, at2)
            else if (where2 === "FROM_END") return list.slice(list.length - at1, list.length - (at2 - 1))
            else if (where2 === "LAST") return list.slice(list.length - at1, list.length)
        } else if (where1 === "FIRST") {
            if (where2 === "FROM_START") return list.slice(0, at2)
            else if (where2 === "FROM_END") return list.slice(0, list.length - (at2 - 1))
            else if (where2 === "LAST") return list.slice(0, list.length)
        }
    } catch (e) {
        onError(e)
    }

    return null
}
