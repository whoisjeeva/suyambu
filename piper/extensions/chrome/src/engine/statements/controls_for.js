import { stringify } from "../util"


export default async function(statement, onError) {
    try {
        this.loopFlow = null
        let pointer = statement.pointer
        let start = await this.executeStatement(stringify(pointer.from))
        start = parseInt(start)
        let end = await this.executeStatement(stringify(pointer.to))
        end = parseInt(end)
        let step = await this.executeStatement(stringify(pointer.step))
        step = parseInt(step)

        let variableName = stringify(pointer.variable)
        let loopBlock = stringify(pointer.loopBlock).trim()

        for (let i = start; i <= end; i += step) {
            this.VARIABLE_STACK[variableName] = i

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
