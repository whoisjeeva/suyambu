import { stringify } from "../util"


export default async function(statement, onError) {
    try {
        return stringify(statement.pointer)
            .replace("\\n", "\n")
            .replace("\\t", "\t")
            .replace("\\r", "\r")
    } catch (e) {
        onError(e)
    }
}
