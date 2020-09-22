#version 330 core

out vec4 out_Color;

uniform vec4 color;

void main(){

    out_Color = vec4(color.xyz/255,color.w);
}