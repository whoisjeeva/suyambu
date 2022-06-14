import { stringify } from "../util"
import procedures_callnoreturn from "./procedures_callnoreturn"


export default async function(statement, onError) {
    try {
        return await procedures_callnoreturn(statement, onError)
    } catch (e) {
        onError(e)
    }
}
