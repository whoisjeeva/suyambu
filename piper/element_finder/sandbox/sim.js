function findSimilarElements(el) {
    let rect = el.getBoundingClientRect()
    let width = rect.width
    let height = rect.height

    let allEls = document.querySelectorAll("*")
    let similarEls = []

    for (let i = 0; i < allEls.length; i++) {
        let simEl = allEls[i]
        let simRect = simEl.getBoundingClientRect()
        let simWidth = simRect.width
        let simHeight = simRect.height

        if (width === simWidth && height === simHeight && simEl.tagName === el.tagName) {
            similarEls.push(simEl)
        }
    }

    return similarEls
}