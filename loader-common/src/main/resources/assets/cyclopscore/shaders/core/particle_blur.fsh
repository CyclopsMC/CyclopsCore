#version 330

#moj_import <minecraft:fog.glsl>
#moj_import <minecraft:dynamictransforms.glsl>

//layout(std140) uniform SamplerInfo {
//    vec2 OutSize;
//    vec2 InSize;
//};

uniform sampler2D Sampler0;
uniform sampler2D Sampler2;

in float sphericalVertexDistance;
in float cylindricalVertexDistance;
in vec2 texCoord0;
in vec4 vertexColor;

out vec4 fragColor;

vec4 box_blur(vec2 BlurDir) {
//    vec2 oneTexel = 1.0 / vec2(512, 256.0);
//    vec2 oneTexel = 1.0 / InSize; // Seems to be difficult to pass SamplerInfo as param into RenderPipeline...
    vec2 oneTexel = 1.0 / textureSize(Sampler0, 0);
    vec2 sampleStep = oneTexel * BlurDir;

    vec4 blurred = vec4(0.0);
    float actualRadius = 4;
    for (float a = -actualRadius + 0.5; a <= actualRadius; a += 2.0) {
        blurred += texture(Sampler0, texCoord0 + sampleStep * a);
    }
    blurred += texture(Sampler0, texCoord0 + sampleStep * actualRadius) / 2.0;
//    return vec4((blurred / (actualRadius + 0.5)).rgb, blurred.a); // This would not blur the alpha value
    return blurred / (actualRadius + 0.5);
}

// This is a combination of vanilla's core/particle.fsh and post/box_blur.fsh and post/entity_outline_box_blur.fsh
void main() {
    vec4 color = texture(Sampler0, texCoord0) * vertexColor * ColorModulator;
    if (color.a < 0.1) {
        discard;
    }
    // We apply box blur in 2 passes: horizontal and vertical
    fragColor = apply_fog(color, sphericalVertexDistance, cylindricalVertexDistance, FogEnvironmentalStart, FogEnvironmentalEnd, FogRenderDistanceStart, FogRenderDistanceEnd, FogColor) * box_blur(vec2(1.0, 0.0)) * box_blur(vec2(0.0, 1.0));
}
