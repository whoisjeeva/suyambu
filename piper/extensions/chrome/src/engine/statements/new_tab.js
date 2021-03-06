import { stringify } from "../util"


export default async function(statement, onError) {
    try {
        await this.browser.createNewWindow()
        await this.browser.newTab()
    } catch (e) {
        onError(e)
    }
}
