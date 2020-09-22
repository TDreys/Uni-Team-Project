#version 330 core

layout(location = 0) in vec3 position;

uniform vec2 size;
uniform vec2 screenPosition;

void main(void){

    vec3 finalpos;
    finalpos.x = (((position.x * size.x) + screenPosition.x) / 1280 * 2)-1;
    finalpos.y = (((position.y * size.y) - screenPosition.y) / 720 * 2) + 1;
    finalpos.z = position.z;
	gl_Position = vec4(finalpos,1.0);
}