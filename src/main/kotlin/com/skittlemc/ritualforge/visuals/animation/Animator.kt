package com.skittlemc.ritualforge.visuals.animation

class Animator {

    private var currentState: String = "idle"
    private var player: AnimationPlayer? = null

    fun setState(state: String) {
        if (state == currentState) return
        currentState = state
        player = null // Will be resolved on next tick
    }

    fun getState(): String = currentState

    fun tick(blender: AnimationBlender) {
        player?.tick()
    }

    fun setPlayer(player: AnimationPlayer) {
        this.player = player
    }
}
