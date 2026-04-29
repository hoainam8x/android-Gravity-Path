package com.duna.gravitypath.data.repository

import com.duna.gravitypath.data.local.json.LevelsJsonDataSource
import com.duna.gravitypath.domain.model.Barrier
import com.duna.gravitypath.domain.model.Fan
import com.duna.gravitypath.domain.model.GameStage
import com.duna.gravitypath.domain.model.Goal
import com.duna.gravitypath.domain.model.Level
import com.duna.gravitypath.domain.model.Rect
import com.duna.gravitypath.domain.model.SurfaceMaterial
import com.duna.gravitypath.domain.model.Vec2
import com.duna.gravitypath.domain.model.ZeroGZone
import com.duna.gravitypath.domain.repository.LevelRepository
import org.json.JSONObject

class LevelRepositoryImpl(
    private val levelsJsonDataSource: LevelsJsonDataSource,
) : LevelRepository {
    override suspend fun getLevels(): List<Level> {
        val root = JSONObject(levelsJsonDataSource.loadRawJson())
        val levelsArray = root.getJSONArray("levels")
        return buildList {
            for (i in 0 until levelsArray.length()) {
                val item = levelsArray.getJSONObject(i)
                add(
                    Level(
                        id = item.getInt("id"),
                        name = item.getString("name"),
                        stage = stageFrom(item.getString("stage")),
                        ballStart = toVec2(item.getJSONArray("ball")),
                        ballRadius = root.optDouble("defaultBallRadius", 14.0).toFloat(),
                        goal = Goal(
                            center = toVec2(item.getJSONArray("goal")),
                            radius = root.optDouble("defaultGoalRadius", 28.0).toFloat(),
                        ),
                        barriers = toBarriers(item),
                        zeroGZones = toZeroG(item),
                        fans = toFans(item),
                    ),
                )
            }
        }
    }

    private fun toBarriers(item: JSONObject): List<Barrier> {
        val array = item.getJSONArray("barriers")
        return buildList {
            for (index in 0 until array.length()) {
                val barrier = array.getJSONObject(index)
                add(
                    Barrier(
                        position = toVec2(barrier.getJSONArray("pos")),
                        length = barrier.getDouble("len").toFloat(),
                        thickness = 12f,
                        rotation = barrier.getDouble("rot").toFloat(),
                        material = materialFrom(barrier.optString("mat", "wood")),
                    ),
                )
            }
        }
    }

    private fun toZeroG(item: JSONObject): List<ZeroGZone> {
        val array = item.optJSONArray("zeroG") ?: return emptyList()
        return buildList {
            for (index in 0 until array.length()) {
                val zoneObj = array.getJSONObject(index)
                val rect = zoneObj.getJSONArray("rect")
                add(
                    ZeroGZone(
                        rect = Rect(
                            x = rect.getDouble(0).toFloat(),
                            y = rect.getDouble(1).toFloat(),
                            width = rect.getDouble(2).toFloat(),
                            height = rect.getDouble(3).toFloat(),
                        ),
                    ),
                )
            }
        }
    }

    private fun toFans(item: JSONObject): List<Fan> {
        val array = item.optJSONArray("fans") ?: return emptyList()
        return buildList {
            for (index in 0 until array.length()) {
                val fanObj = array.getJSONObject(index)
                add(
                    Fan(
                        position = toVec2(fanObj.getJSONArray("pos")),
                        force = toVec2(fanObj.getJSONArray("force")),
                    ),
                )
            }
        }
    }

    private fun toVec2(array: org.json.JSONArray): Vec2 =
        Vec2(
            x = array.getDouble(0).toFloat(),
            y = array.getDouble(1).toFloat(),
        )

    private fun stageFrom(raw: String): GameStage = when (raw.lowercase()) {
        "tutorial" -> GameStage.TUTORIAL
        "materials" -> GameStage.MATERIALS
        "advanced" -> GameStage.ADVANCED
        else -> GameStage.TUTORIAL
    }

    private fun materialFrom(raw: String): SurfaceMaterial = when (raw.lowercase()) {
        "glass" -> SurfaceMaterial.GLASS
        "rubber" -> SurfaceMaterial.RUBBER
        else -> SurfaceMaterial.WOOD
    }
}
