package org.luxons.sevenwonders.ui.components.gameBrowser

import blueprintjs.core.*
import blueprintjs.icons.*
import kotlinx.css.*
import kotlinx.html.js.*
import org.luxons.sevenwonders.ui.redux.*
import react.*
import react.dom.*
import styled.*

private external interface CreateGameFormProps : PropsWithChildren {
    var createGame: (String) -> Unit
}

private external interface CreateGameFormState : State {
    var gameName: String
}

private class CreateGameForm(props: CreateGameFormProps) : RComponent<CreateGameFormProps, CreateGameFormState>(props) {

    override fun CreateGameFormState.init(props: CreateGameFormProps) {
        gameName = ""
    }

    override fun RBuilder.render() {
        styledDiv {
            css {
                display = Display.flex
                flexDirection = FlexDirection.row
                justifyContent = JustifyContent.spaceBetween
            }
            form {
                attrs {
                    onSubmitFunction = { e ->
                        e.preventDefault()
                        createGame()
                    }
                }

                bpInputGroup(
                    large = true,
                    placeholder = "Game name",
                    onChange = { e ->
                        val input = e.currentTarget
                        state.gameName = input.value
                    },
                    rightElement = createGameButton(),
                )
            }
        }
    }

    private fun createGameButton() = buildElement {
        bpButton(minimal = true, intent = Intent.PRIMARY, icon = IconNames.ADD, onClick = { e ->
            e.preventDefault() // prevents refreshing the page when pressing Enter
            createGame()
        })
    }

    private fun createGame() {
        props.createGame(state.gameName)
    }
}

val createGameForm: ComponentClass<PropsWithChildren> = connectDispatch(CreateGameForm::class) { dispatch, _ ->
    createGame = { name -> dispatch(RequestCreateGame(name)) }
}
