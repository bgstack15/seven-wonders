package org.luxons.sevenwonders.ui.components.game

import kotlinx.css.*
import kotlinx.css.properties.*
import kotlinx.html.title
import org.luxons.sevenwonders.model.api.PlayerDTO
import org.luxons.sevenwonders.model.cards.CardBack
import org.luxons.sevenwonders.ui.redux.connectStateWithOwnProps
import react.*
import react.dom.attrs
import styled.animation
import styled.css
import styled.styledDiv
import styled.styledImg

external interface PlayerPreparedCardProps : PropsWithChildren {
    var playerDisplayName: String
    var cardBack: CardBack?
}

external interface PlayerPreparedCardContainerProps : PropsWithChildren {
    var playerDisplayName: String
    var username: String
}

fun RBuilder.playerPreparedCard(player: PlayerDTO) = playerPreparedCard {
    attrs {
        this.playerDisplayName = player.displayName
        this.username = player.username
    }
}

private class PlayerPreparedCard(props: PlayerPreparedCardProps) : RComponent<PlayerPreparedCardProps, State>(props) {

    override fun RBuilder.render() {
        val cardBack = props.cardBack
        val sideSize = 30.px
        styledDiv {
            css {
                width = sideSize
                height = sideSize
            }
            attrs {
                title = if (cardBack == null) {
                    "${props.playerDisplayName} is still thinking…"
                } else {
                    "${props.playerDisplayName} is ready to play this turn"
                }
            }
            if (cardBack != null) {
                cardBackImage(cardBack) {
                    css {
                        maxHeight = sideSize
                    }
                }
            } else {
                styledImg(src = "images/gear-50.png") {
                    css {
                        maxHeight = sideSize
                        animation(
                            duration = 1.5.s,
                            iterationCount = IterationCount.infinite,
                            timing = cubicBezier(0.2, 0.9, 0.7, 1.3)
                        ) {
                            to {
                                transform { rotate(360.deg) }
                            }
                        }
                    }
                }
            }
        }
    }
}

private val playerPreparedCard = connectStateWithOwnProps(
    clazz = PlayerPreparedCard::class,
    mapStateToProps = { state, ownProps: PlayerPreparedCardContainerProps ->
        playerDisplayName = ownProps.playerDisplayName
        cardBack = state.gameState?.preparedCardsByUsername?.get(ownProps.username)
    },
)
