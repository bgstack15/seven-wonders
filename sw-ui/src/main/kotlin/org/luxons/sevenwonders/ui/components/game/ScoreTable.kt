package org.luxons.sevenwonders.ui.components.game

import blueprintjs.core.*
import csstype.*
import kotlinx.css.*
import kotlinx.css.Display
import kotlinx.css.FlexDirection
import kotlinx.css.TextAlign
import kotlinx.css.VerticalAlign
import kotlinx.css.px
import kotlinx.css.rem
import kotlinx.html.TD
import kotlinx.html.TH
import org.luxons.sevenwonders.model.api.PlayerDTO
import org.luxons.sevenwonders.model.score.ScoreBoard
import org.luxons.sevenwonders.model.score.ScoreCategory
import org.luxons.sevenwonders.ui.components.GlobalStyles
import org.luxons.sevenwonders.ui.utils.*
import react.RBuilder
import react.dom.*
import styled.*

fun RBuilder.scoreTableOverlay(scoreBoard: ScoreBoard, players: List<PlayerDTO>, leaveGame: () -> Unit) {
    bpOverlay(isOpen = true) {
        bpCard {
            attrs {
                val fixedCenterClass = GlobalStyles.getClassName { it::fixedCenter }
                val scoreBoardClass = GameStyles.getClassName { it::scoreBoard }
                className = ClassName("$fixedCenterClass $scoreBoardClass")
            }
            styledDiv {
                css {
                    display = Display.flex
                    flexDirection = FlexDirection.column
                    alignItems = Align.center
                    +GameStyles.scoreBoard // loads the styles so that they can be picked up by bpCard
                }
                styledH1 {
                    css {
                        marginTop = 0.px
                    }
                    +"Score Board"
                }
                scoreTable(scoreBoard, players)
                styledDiv {
                    css {
                        marginTop = 1.rem
                    }
                    bpButton(intent = Intent.WARNING, rightIcon = "log-out", large = true, onClick = { leaveGame() }) {
                        +"LEAVE"
                    }
                }
            }
        }
    }
}

private fun RBuilder.scoreTable(scoreBoard: ScoreBoard, players: List<PlayerDTO>) {
    bpHtmlTable(bordered = false, interactive = true) {
        thead {
            tr {
                centeredTh { +"Rank" }
                centeredTh {
                    attrs { colSpan = "2" }
                    +"Player"
                }
                centeredTh { +"Score" }
                ScoreCategory.values().forEach {
                    centeredTh { +it.title }
                }
            }
        }
        tbody {
            scoreBoard.scores.forEachIndexed { index, score ->
                val player = players[score.playerIndex]
                tr {
                    centeredTd { ordinal(scoreBoard.ranks[index]) }
                    centeredTd { bpIcon(player.icon?.name ?: "user", size = 25) }
                    styledTd {
                        inlineStyles {
                            verticalAlign = VerticalAlign.middle
                        }
                        +player.displayName
                    }
                    centeredTd {
                        bpTag(large = true, round = true, minimal = true) {
                            attrs {
                                this.className = GameStyles.getTypedClassName { it::totalScore }
                            }
                            +"${score.totalPoints}"
                        }
                    }
                    ScoreCategory.values().forEach { cat ->
                        centeredTd {
                            bpTag(large = true, round = true, icon = cat.icon, fill = true) {
                                attrs {
                                    this.className = classNameForCategory(cat)
                                }
                                +"${score.pointsByCategory[cat]}"
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun RBuilder.ordinal(value: Int) {
    +"$value"
    sup { +value.ordinalIndicator() }
}

private fun Int.ordinalIndicator() = when {
    this % 10 == 1 && this != 11 -> "st"
    this % 10 == 2 && this != 12 -> "nd"
    this % 10 == 3 && this != 13 -> "rd"
    else -> "th"
}

private fun RBuilder.centeredTh(block: RDOMBuilder<TH>.() -> Unit) {
    th {
        // inline styles necessary to overcome blueprintJS overrides
        inlineStyles {
            textAlign = TextAlign.center
            verticalAlign = VerticalAlign.middle
        }
        block()
    }
}

private fun RBuilder.centeredTd(block: RDOMBuilder<TD>.() -> Unit) {
    td {
        // inline styles necessary to overcome blueprintJS overrides
        inlineStyles {
            textAlign = TextAlign.center
            verticalAlign = VerticalAlign.middle
        }
        block()
    }
}

private fun classNameForCategory(cat: ScoreCategory): ClassName = GameStyles.getTypedClassName {
    when (cat) {
        ScoreCategory.CIVIL -> it::civilScore
        ScoreCategory.SCIENCE -> it::scienceScore
        ScoreCategory.MILITARY -> it::militaryScore
        ScoreCategory.TRADE -> it::tradeScore
        ScoreCategory.GUILD -> it::guildScore
        ScoreCategory.WONDER -> it::wonderScore
        ScoreCategory.GOLD -> it::goldScore
    }
}

private val ScoreCategory.icon: String
    get() = when (this) {
        ScoreCategory.CIVIL -> "office"
        ScoreCategory.SCIENCE -> "lab-test"
        ScoreCategory.MILITARY -> "cut"
        ScoreCategory.TRADE -> "swap-horizontal"
        ScoreCategory.GUILD -> "clean" // stars
        ScoreCategory.WONDER -> "symbol-triangle-up"
        ScoreCategory.GOLD -> "dollar"
    }

// Potentially useful emojis:
// Greek temple:  🏛
// Cog (science): ⚙️
// Swords (war):  ⚔️
// Gold bag:      💰
