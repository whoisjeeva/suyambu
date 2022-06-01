import { stringify } from "../util"


export default async function(statement, onError) {
    try {
        this.loopFlow = null
        let pointer = statement.pointer
        let times = await this.executeStatement(stringify(pointer.times)) || 0
        times = parseInt(times)
        let loopBlock = stringify(pointer.loopBlock).trim()
        if (loopBlock !== "") {
            for (let i = 0; i < times; i++) {
                await this.execute(loopBlock)
                if (this.isTerminated) break
                if (this.loopFlow === "BREAK") {
                    this.loopFlow = null
                    break
                } else if (this.loopFlow === "CONTINUE") {
                    this.loopFlow = null
                    continue
                }

                if (this.isFunctionReturn != null) break
            }
        }
    } catch (e) {
        onError(e)
    }
}
