import { stringify } from "../util"


export default async function(statement, onError) {
    let tryBlock
    let catchBlock
    try {
        let pointer = statement.pointer
        tryBlock = stringify(pointer.try)
        catchBlock = stringify(pointer.catch)
    } catch (e) {
        onError(e)
        return
    }


    try {
        this.canShowErrorAlert = false
        await this.execute(tryBlock)
        this.canShowErrorAlert = true
        if (this.isTerminated) {
            this.isTerminated = false
            throw new Error("Try failed")
        }
    } catch(e) {
        try {
            await this.execute(catchBlock)
        } catch (e) {
            onError(e)
        }
    }
}
