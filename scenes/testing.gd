extends Node3D

@export var player: Node3D
@export var enemy: Node3D

func _process(delta: float) -> void:
	print(enemy.global_position.distance_to(player.global_position))
