import { stringify } from "../util"


export default async function(statement, onError) {
    try {
        let pointer = stringify(statement.pointer)
        if (pointer === "DESKTOP") {
            await chrome.declarativeNetRequest.updateEnabledRulesets({ enableRulesetIds: ["desktop_user_agent_rule"], disableRulesetIds: ["mobile_user_agent_rule"] })
        } else if (pointer === "MOBILE") {
            await chrome.declarativeNetRequest.updateEnabledRulesets({ enableRulesetIds: ["mobile_user_agent_rule"], disableRulesetIds: ["desktop_user_agent_rule"] })
        }
    } catch (e) {
        onError(e)
    }
}
