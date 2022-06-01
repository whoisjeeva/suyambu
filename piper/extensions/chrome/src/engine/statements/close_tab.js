import { stringify } from "../util"


export default async function(statement, onError) {
    try {
        let index = await this.executeStatement(stringify(statement.pointer))
        await this.browser.closeTab(index - 1)
    } catch (e) {
        onError(e)
    }
}
