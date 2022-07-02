package org.luxons.sevenwonders.ui.components.home

import blueprintjs.core.Intent
import blueprintjs.core.bpButton
import blueprintjs.core.bpInputGroup
import blueprintjs.icons.IconNames
import kotlinx.css.*
import kotlinx.html.js.onSubmitFunction
import org.luxons.sevenwonders.ui.redux.RequestChooseName
import org.luxons.sevenwonders.ui.redux.connectDispatch
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.events.Event
import react.*
import styled.css
import styled.styledDiv
import styled.styledForm

private interface ChooseNameFormProps : PropsWithChildren {
    var chooseUsername: (String) -> Unit
}

private external interface ChooseNameFormState : State {
    var username: String
}

private class ChooseNameForm(props: ChooseNameFormProps) : Component<ChooseNameFormProps, ChooseNameFormState>(props) {

    override fun ChooseNameFormState.init(props: ChooseNameFormProps) {
        username = ""
    }

    override fun RBuilder.render() {
        styledForm {
            css {
                display = Display.flex
                flexDirection = FlexDirection.row
            }
            attrs.onSubmitFunction = { e -> chooseUsername(e) }
            randomNameButton()
            spacer()
            bpInputGroup(
                large = true,
                placeholder = "Username",
                rightElement = submitButton(),
                value = state.username,
                onChange = { e ->
                    val input = e.currentTarget as HTMLInputElement
                    setState {
                        username = input.value
                    }
                },
            )
        }
    }

    private fun submitButton(): ReactElement = buildElement {
        bpButton(
            minimal = true,
            icon = IconNames.ARROW_RIGHT,
            intent = Intent.PRIMARY,
            onClick = { e -> chooseUsername(e) },
        )
    }

    private fun RBuilder.randomNameButton() {
        bpButton(
            title = "Generate random name",
            large = true,
            icon = IconNames.RANDOM,
            intent = Intent.PRIMARY,
            onClick = { fillRandomUsername() },
        )
    }

    private fun fillRandomUsername() {
        setState { username = randomGreekName() }
    }

    private fun chooseUsername(e: Event) {
        e.preventDefault()
        props.chooseUsername(state.username)
    }

    // TODO this is so bad I'm dying inside
    private fun RBuilder.spacer() {
        styledDiv {
            css {
                margin(2.px)
            }
        }
    }

    override fun render(): ReactNode? {
        TODO("Not yet implemented")
    }
}

val chooseNameForm: ComponentClass<PropsWithChildren> = connectDispatch(ChooseNameForm::class) { dispatch, _ ->
    chooseUsername = { name -> dispatch(RequestChooseName(name)) }
}
