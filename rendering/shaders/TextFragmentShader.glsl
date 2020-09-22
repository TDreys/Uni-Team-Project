#version 330 core

in vec2 passTextureCoords;

out vec4 out_Color;

uniform sampler2D textureSampler;
uniform vec3 color;

void main(){

    out_Color = texture(textureSampler,passTextureCoords);
    if(out_Color.w != 0.0)
    {
        out_Color = vec4(color/255,out_Color.w);
    }
}