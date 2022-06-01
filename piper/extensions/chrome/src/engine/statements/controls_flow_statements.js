import { stringify } from "../util"


export default async function(statement, onError) {
    try {
        this.loopFlow = stringify(statement.pointer.flow)
    } catch (e) {
        onError(e)
    }
}
