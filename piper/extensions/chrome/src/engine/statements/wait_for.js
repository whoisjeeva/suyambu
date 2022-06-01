import { stringify, sleep } from "../util"


export default async function(statement, onError) {
    try {
        let secs = parseFloat(statement.pointer)
        await sleep(parseInt(secs * 1000))
    } catch (e) {
        onError(e)
    }
}
