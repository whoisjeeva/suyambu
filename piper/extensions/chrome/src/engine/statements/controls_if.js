import { stringify } from "../util"


export default async function(statement, onError) {
    try {
        for (let block of statement.pointer) {
            if ("else" in block) {
                await this.execute(stringify(block.else))
            } else {
                let cond = await this.executeStatement(stringify(block.cond))
                if (typeof cond === "boolean" && cond) {
                    let doBlock = stringify(block.do).trim()
                    if (doBlock !== "") await this.execute(doBlock)
                    break
                }
            }
        }
    } catch (e) {
        onError(e)
    }
}
