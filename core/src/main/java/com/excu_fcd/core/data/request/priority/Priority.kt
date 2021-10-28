package com.excu_fcd.core.data.request.priority

interface Priority {

    companion object {
        fun high(): Priority = object : Priority {

        }

        fun low(): Priority = object : Priority {

        }

        fun middle(): Priority = object : Priority {

        }

        fun empty(): Priority = object : Priority {

        }
    }

}