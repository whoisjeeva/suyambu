import { stringify } from "../util"


function titleCase(str) {
    return str.replace(/\w\S*/g, function(txt){return txt.charAt(0).toUpperCase() + txt.substr(1).toLowerCase();});
}


export default async function(statement, onError) {
    try {
        let pointer = statement.pointer
        let value = await this.executeStatement(stringify(pointer.value))
        
        switch(stringify(pointer.action)) {
            case "UPPERCASE":
                return value.toUpperCase()
            case "LOWERCASE":
                return value.toLowerCase()
            case "TITLECASE":
                return titleCase(value)
            default:
                return value
        }
    } catch (e) {
        onError(e)
    }

    return null
}
