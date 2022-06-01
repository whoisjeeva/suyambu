import { $ } from "./dom"
import ElementSelectorPage from "./pages/ElementSelectorPage"
import HomePage from "./pages/HomePage"


chrome.storage.sync.get("page", data => {
    if (data.page === "html_elements") {
        new ElementSelectorPage("#app").render()
    } else {
        new HomePage("#app").render()
    }
})
