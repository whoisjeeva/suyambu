import { stringify } from "../util"


export default async function(statement, onError) {
    try {
        let pointer = statement.pointer
        let value = await this.executeStatement(stringify(pointer.value))
        value = stringify(value)
        let at = await this.executeStatement(stringify(pointer.at))
        
        switch(stringify(pointer.action)) {
            case "FROM_START":
                at = parseInt(at)
                return value[at - 1]
            case "FROM_END":
                at = parseInt(at)
                return value[value.length - at]
            case "FIRST":
                return value[0]
            case "LAST":
                return value[value.length - 1]
            case "RANDOM":
                return value[Math.floor(Math.random() * value.length)]
        }
    } catch (e) {
        console.log(e)
        onError(e)
    }

    return null
}
