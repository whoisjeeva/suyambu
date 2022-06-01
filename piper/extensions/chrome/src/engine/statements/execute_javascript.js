import { stringify } from "../util"


export default async function(statement, onError) {
    try {
        let pointer = await this.executeStatement(stringify(statement.pointer))
        await this.browser.executeScript(code => {
            eval(code)
            return true
        }, [pointer])
    } catch (e) {
        onError(e)
    }
}
