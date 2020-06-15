package org.luxons.sevenwonders.ui.components.game

import com.palantir.blueprintjs.PopoverPosition
import com.palantir.blueprintjs.bpDivider
import com.palantir.blueprintjs.bpPopover
import kotlinx.css.*
import kotlinx.html.DIV
import org.luxons.sevenwonders.model.api.PlayerDTO
import org.luxons.sevenwonders.model.boards.Board
import org.luxons.sevenwonders.ui.components.gameBrowser.playerInfo
import react.RBuilder
import react.ReactElement
import react.buildElement
import styled.*

enum class BoardSummarySide(
    val tokenCountPosition: TokenCountPosition,
    val alignment: Align,
    val popoverPosition: PopoverPosition
) {
    LEFT(TokenCountPosition.RIGHT, Align.flexStart, PopoverPosition.RIGHT),
    TOP(TokenCountPosition.OVER, Align.flexStart, PopoverPosition.BOTTOM),
    RIGHT(TokenCountPosition.LEFT, Align.flexEnd, PopoverPosition.LEFT)
}

fun RBuilder.boardSummaryWithPopover(
    player: PlayerDTO,
    board: Board,
    boardSummarySide: BoardSummarySide,
    block: StyledDOMBuilder<DIV>.() -> Unit = {}
) {
    val popoverClass = GameStyles.getClassName { it::fullBoardPreviewPopover }
    bpPopover(
        content = createFullBoardPreview(board),
        position = boardSummarySide.popoverPosition,
        popoverClassName = popoverClass
    ) {
        boardSummary(player, board, boardSummarySide, block)
    }
}

private fun createFullBoardPreview(board: Board): ReactElement = buildElement {
    boardComponent(board = board) {
        css {
            +GameStyles.fullBoardPreview
        }
    }
}

private fun RBuilder.boardSummary(
    player: PlayerDTO,
    board: Board,
    boardSummarySide: BoardSummarySide,
    block: StyledDOMBuilder<DIV>.() -> Unit = {}
) {
    styledDiv {
        css {
            display = Display.flex
            flexDirection = FlexDirection.column
            alignItems = boardSummarySide.alignment
            padding(all = 0.5.rem)
            backgroundColor = Color.paleGoldenrod.withAlpha(0.5)
            zIndex = 50 // above table cards

            hover {
                backgroundColor = Color.paleGoldenrod
            }
        }

        playerInfo(player, iconSize = 25)
        styledHr {
            css {
                margin(vertical = 0.5.rem)
                width = 100.pct
            }
        }
        styledDiv {
            css {
                display = Display.flex
                flexDirection = if (boardSummarySide == BoardSummarySide.TOP) FlexDirection.row else FlexDirection.column
                alignItems = boardSummarySide.alignment
            }
            val tokenSize = 2.rem
            generalCounts(board, tokenSize, boardSummarySide.tokenCountPosition)
            bpDivider()
            scienceTokens(board, tokenSize, boardSummarySide.tokenCountPosition)
        }
        block()
    }
}

private fun StyledDOMBuilder<DIV>.generalCounts(
    board: Board,
    tokenSize: LinearDimension,
    countPosition: TokenCountPosition
) {
    goldIndicator(amount = board.gold, imgSize = tokenSize, amountPosition = countPosition)
    tokenWithCount(
        tokenName = "laurel-blue",
        count = board.bluePoints,
        imgSize = tokenSize,
        countPosition = countPosition,
        brightText = countPosition == TokenCountPosition.OVER
    )
    tokenWithCount(
        tokenName = "military",
        count = board.military.nbShields,
        imgSize = tokenSize,
        countPosition = countPosition,
        brightText = countPosition == TokenCountPosition.OVER
    )
}

private fun RBuilder.scienceTokens(
    board: Board,
    tokenSize: LinearDimension,
    sciencePosition: TokenCountPosition
) {
    tokenWithCount(
        tokenName = "compass",
        count = board.science.nbCompasses,
        imgSize = tokenSize,
        countPosition = sciencePosition,
        brightText = sciencePosition == TokenCountPosition.OVER
    )
    tokenWithCount(
        tokenName = "cog",
        count = board.science.nbWheels,
        imgSize = tokenSize,
        countPosition = sciencePosition,
        brightText = sciencePosition == TokenCountPosition.OVER
    )
    tokenWithCount(
        tokenName = "tablet",
        count = board.science.nbTablets,
        imgSize = tokenSize,
        countPosition = sciencePosition,
        brightText = sciencePosition == TokenCountPosition.OVER
    )
}