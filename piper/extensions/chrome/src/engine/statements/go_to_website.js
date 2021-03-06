import { stringify, sleep } from "../util"


export default async function(statement, onError) {
    try {
        let pointer = stringify(statement["pointer"])
        let url = stringify(await this.executeStatement(pointer))
        await this.browser.createNewWindow()
        console.log(`Opening ${url}`)
        await sleep(1000)
        if (url.startsWith("http://") || url.startsWith("https://")) {
            await this.browser.loadUrl(url)
        } else {
            await this.browser.loadUrl(`http://${url}`)
        }
        await this.browser.loadingWait()
    } catch (e) {
        onError(e)
    }
}
