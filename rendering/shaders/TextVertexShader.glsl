#version 330 core

layout(location = 0) in vec3 position;
layout(location = 2) in vec2 textureCoords;

out vec2 passTextureCoords;

uniform vec2 screenPosition;

void main(void){

    vec3 final = position;
    final.x = (position.x + screenPosition.x)/1280 - 1;
    final.y = -(position.y + screenPosition.y)/720 + 1;
    final.z = position.z;
	gl_Position = vec4(final,1.0);

	passTextureCoords = textureCoords;
}