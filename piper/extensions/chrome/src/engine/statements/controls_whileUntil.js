import { stringify } from "../util"


export default async function(statement, onError) {
    try {
        this.loopFlow = null
        let pointer = statement.pointer
        let loopBlock = stringify(pointer.loopBlock)
        let mode = stringify(pointer.mode)

        while(true) {
            let cond
            try {
                cond = await this.executeStatement(stringify(pointer.cond))
            } catch (e) {
                cond = false
            }

            if (mode === "UNTIL") {
                if (cond) break
            } else {
                if (!cond) break
            }

            await this.execute(loopBlock)

            if (this.isTerminated) break
            if (this.loopFlow === "BREAK") {
                this.loopFlow = null
                break
            } else if (this.loopFlow === "CONTINUE") {
                this.loopFlow = null
                continue
            }

            if (this.isFunctionReturn !== null) break
        }
    } catch (e) {
        onError(e)
    }
}
