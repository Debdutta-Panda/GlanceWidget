package com.debduttapanda.glancewidget

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.LocalContext
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.state.GlanceStateDefinition
import androidx.glance.state.PreferencesGlanceStateDefinition
import androidx.glance.text.Text


class QuotesWidgetReceiver : GlanceAppWidgetReceiver() {

    override val glanceAppWidget: GlanceAppWidget = QuotesWidget()

}

class QuotesWidget : GlanceAppWidget() {

    override val stateDefinition: GlanceStateDefinition<*> = PreferencesGlanceStateDefinition

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {

            val editing = remember{mutableStateOf(false)}
            val value = remember{mutableStateOf("")}
            if(editing.value){
                Column(
                    modifier = GlanceModifier
                        .background(Color.Green)
                        .padding(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Text(value.value)
                    Row{
                        repeat(3){
                            MyButton(text = "${it+1}") { value.value += "${it+1}" }
                        }
                    }
                    Row{
                        repeat(3){
                            MyButton(text = "${it+4}") { value.value += "${it+4}" }
                        }
                    }
                    Row{
                        repeat(3){
                            MyButton(text = "${it+7}") { value.value += "${it+7}" }
                        }
                    }
                    Row{
                        Image(
                            provider = ImageProvider(R.drawable.close),
                            contentDescription = "Close",
                            modifier = GlanceModifier
                                .size(40.dp)
                                .background(Color.Green)
                                .clickable {
                                    value.value = ""
                                }
                                .padding(8.dp)
                        )
                        Image(
                            provider = ImageProvider(R.drawable.back),
                            contentDescription = "Back",
                            modifier = GlanceModifier
                                .size(40.dp)
                                .background(Color.Green)
                                .clickable {
                                    val text = value.value
                                    value.value = text.substring(0,text.length-1)
                                }
                                .padding(8.dp)
                        )
                        Image(
                            provider = ImageProvider(R.drawable.send),
                            contentDescription = "Send",
                            modifier = GlanceModifier
                                .size(40.dp)
                                .background(Color.Green)
                                .clickable {
                                    editing.value = false
                                    send(context, value.value)
                                    value.value = ""
                                }
                                .padding(8.dp)
                        )
                    }
                }
            }
            else{
                Image(
                    provider = ImageProvider(R.drawable.whatsapp_send),
                    contentDescription = "Send",
                    modifier = GlanceModifier
                        .clickable {
                            editing.value = true
                        }
                )
            }
        }
    }

    private fun send(context: Context, mobile: String) {
        val msg = "Hi"
        context.startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://api.whatsapp.com/send?phone=$mobile&text=$msg")
            )
                .apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }
        )
    }

}

@Composable
fun MyButton(
    text: String,
    action: ()->Unit
){
    Box(
        modifier = GlanceModifier
            .size(40.dp)
            .clickable {
                action()
            },
        contentAlignment = Alignment.Center
    ){
        Text(text)
    }
}
