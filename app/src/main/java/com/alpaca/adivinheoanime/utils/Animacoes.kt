package com.alpaca.adivinheoanime.utils

import android.content.Context
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.alpaca.adivinheoanime.R

class Animacoes {

    companion object {
        fun animarFadeIn(context: Context): Animation {
            val scaleUp = AnimationUtils.loadAnimation(context, R.anim.fade_in)
            scaleUp.duration = 300
            return scaleUp
        }

        fun animarFadeOut(context: Context): Animation {
            val scaleUp = AnimationUtils.loadAnimation(context, R.anim.fade_out)
            scaleUp.duration = 300
            return scaleUp
        }

        fun animarScaleDown(context: Context): Animation {
            val scaleUp = AnimationUtils.loadAnimation(context, R.anim.scale_down)
            scaleUp.duration = 300
            return scaleUp
        }

        fun animarScaleUp(context: Context): Animation {
            val scaleUp = AnimationUtils.loadAnimation(context, R.anim.scale_up)
            scaleUp.duration = 300
            return scaleUp
        }
        fun animarSlideLeft(context: Context): Animation {
            val scaleUp = AnimationUtils.loadAnimation(context, R.anim.slide_left)
            scaleUp.duration = 300
            return scaleUp
        }
        fun animarSlideRight(context: Context): Animation {
            val scaleUp = AnimationUtils.loadAnimation(context, R.anim.slide_right)
            scaleUp.duration = 300
            return scaleUp
        }
    }
}