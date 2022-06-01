import { stringify } from "../util"


export default async function(statement, onError) {
    try {
        this.browser.newTab()
    } catch (e) {
        onError(e)
    }
}
