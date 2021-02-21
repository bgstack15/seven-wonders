package org.luxons.sevenwonders.ui.components.gameBrowser

import com.palantir.blueprintjs.Classes
import com.palantir.blueprintjs.bpCard
import kotlinx.css.*
import kotlinx.html.classes
import org.luxons.sevenwonders.ui.components.GlobalStyles
import react.RBuilder
import react.dom.h1
import styled.css
import styled.getClassName
import styled.styledDiv
import styled.styledH2

fun RBuilder.gameBrowser() = styledDiv {
    css {
        +GlobalStyles.fullscreen
        +GlobalStyles.zeusBackground
        padding(all = 1.rem)
    }
    styledDiv {
        attrs {
            classes += Classes.DARK
        }
        css {
            margin(horizontal = LinearDimension.auto)
            maxWidth = 60.rem
        }
        styledDiv {
            css {
                display = Display.flex
                justifyContent = JustifyContent.spaceBetween
            }
            h1 { +"Games" }
            currentPlayerInfo()
        }

        bpCard(className = GameBrowserStyles.getClassName { it::createGameCard }) {
            styledH2 {
                css { +GameBrowserStyles.cardTitle }
                +"Create a Game"
            }
            createGameForm {}
        }

        bpCard {
            styledH2 {
                css { +GameBrowserStyles.cardTitle }
                +"Join a Game"
            }
            gameList()
        }
    }
}
