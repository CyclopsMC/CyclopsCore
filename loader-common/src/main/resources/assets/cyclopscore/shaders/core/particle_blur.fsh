#version 330
#extension GL_ARB_separate_shader_objects : require

#include <minecraft:fog.glsl>
#include <minecraft:dynamictransforms.glsl>
#include <minecraft:oit.glsl>

uniform sampler2D Sampler0;

layout(location = 0) in float sphericalVertexDistance;
layout(location = 1) in float cylindricalVertexDistance;
layout(location = 2) in vec2 texCoord0;
layout(location = 3) in vec4 vertexColor;

#ifndef OIT_ALPHA_ONLY
layout(location = 0) out vec4 fragColor;
#endif

vec4 box_blur(vec2 BlurDir) {
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

vec4 calculateFinalColor(vec4 color) {
    #ifdef OIT_ACCUMULATE
    color = sampleColorForAccumulation(color);
    vec4 fogColor = vec4(FogColor.rgb * color.a, FogColor.a);
    #else
    vec4 fogColor = FogColor;
    #endif
    return apply_fog(color, sphericalVertexDistance, cylindricalVertexDistance, FogEnvironmentalStart, FogEnvironmentalEnd, FogRenderDistanceStart, FogRenderDistanceEnd, fogColor);
}

// This is a combination of vanilla's core/particle.fsh and post/box_blur.fsh and post/entity_outline_box_blur.fsh
void main() {
    vec4 color = texture(Sampler0, texCoord0) * vertexColor * ColorModulator;
    if (color.a < 0.1) {
        discard;
    }
    #ifdef OIT_ALPHA_ONLY
    executeAlphaOnlyPhase(gl_FragCoord.z, color.a);
    #else
    // We apply box blur in 2 passes: horizontal and vertical
    fragColor = calculateFinalColor(color) * box_blur(vec2(1.0, 0.0)) * box_blur(vec2(0.0, 1.0));
    #endif
}
