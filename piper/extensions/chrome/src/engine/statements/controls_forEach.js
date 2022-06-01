import { stringify } from "../util"


export default async function(statement, onError) {
    try {
        this.loopFlow = null
        let pointer = statement.pointer
        let list = await this.executeStatement(stringify(pointer.list))
        let loopBlock = stringify(pointer.loopBlock).trim()
        let variableName = stringify(pointer.variable)

        for (let i = 0; i < list.length; i++) {
            this.VARIABLE_STACK[variableName] = list[i]
            if (loopBlock !== "") await this.execute(loopBlock)
            
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
