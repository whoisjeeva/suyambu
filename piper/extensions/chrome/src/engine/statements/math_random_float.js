import { stringify } from "../util"


export default async function(statement, onError) {
    try {
        return Math.random()
    } catch (e) {
        onError(e)
    }
    return 0.0
}
