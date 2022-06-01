import { stringify } from "../util"


export default async function(statement, onError) {
    try {
        return statement.pointer
    } catch (e) {
        onError(e)
    }
}
